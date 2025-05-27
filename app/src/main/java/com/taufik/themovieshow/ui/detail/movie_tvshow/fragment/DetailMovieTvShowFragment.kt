package com.taufik.themovieshow.ui.detail.movie_tvshow.fragment

import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.SnapHelper
import com.taufik.themovieshow.R
import com.taufik.themovieshow.base.fragment.BaseFragment
import com.taufik.themovieshow.base.helper.BaseCastItemImpl
import com.taufik.themovieshow.base.helper.BaseDetailData
import com.taufik.themovieshow.base.helper.BaseSimilarItemImpl
import com.taufik.themovieshow.base.helper.MovieDetailWrapper
import com.taufik.themovieshow.base.helper.TvShowDetailWrapper
import com.taufik.themovieshow.databinding.FragmentDetailMovieTvShowBinding
import com.taufik.themovieshow.ui.detail.movie_tvshow.adapter.MovieTvShowCastAdapter
import com.taufik.themovieshow.ui.detail.movie_tvshow.adapter.MovieTvShowSimilarAdapter
import com.taufik.themovieshow.ui.detail.movie_tvshow.adapter.MovieTvShowTrailerVideoAdapter
import com.taufik.themovieshow.ui.detail.movie_tvshow.viewmodel.DetailMovieTvShowViewModel
import com.taufik.themovieshow.ui.favorite.helper.FavoriteAction
import com.taufik.themovieshow.ui.movie.adapter.ReviewsAdapter
import com.taufik.themovieshow.utils.CommonDateFormatConstants
import com.taufik.themovieshow.utils.extensions.applySystemBarBottomPadding
import com.taufik.themovieshow.utils.extensions.convertDate
import com.taufik.themovieshow.utils.extensions.hideView
import com.taufik.themovieshow.utils.extensions.loadImage
import com.taufik.themovieshow.utils.extensions.navigateToDetailMovieTvShow
import com.taufik.themovieshow.utils.extensions.observeNetworkResult
import com.taufik.themovieshow.utils.extensions.popBackStack
import com.taufik.themovieshow.utils.extensions.share
import com.taufik.themovieshow.utils.extensions.showSuccessToastyIcon
import com.taufik.themovieshow.utils.extensions.showTrailerVideo
import com.taufik.themovieshow.utils.extensions.showView
import com.taufik.themovieshow.utils.extensions.stringFormat
import com.taufik.themovieshow.utils.extensions.stringReleaseFormat
import com.taufik.themovieshow.utils.extensions.toMovieBaseVideoItemList
import com.taufik.themovieshow.utils.extensions.toRating
import com.taufik.themovieshow.utils.extensions.toTvShowBaseVideoItemList
import com.taufik.themovieshow.utils.extensions.toggleVisibilityIf
import com.taufik.themovieshow.utils.helper.DynamicSnapHelper
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class DetailMovieTvShowBindingFragment : BaseFragment<FragmentDetailMovieTvShowBinding>() {

    private val detailMovieTvShowViewModel: DetailMovieTvShowViewModel by viewModels()

    private val reviewsAdapter by lazy { ReviewsAdapter() }
    private val castAdapter by lazy { MovieTvShowCastAdapter() }
    private var trailerVideoAdapter: MovieTvShowTrailerVideoAdapter? = null
    private var similarAdapter: MovieTvShowSimilarAdapter? = null

    private val from: FROM by lazy {
        val fromString = arguments?.getString(EXTRA_FROM)
        FROM.entries.find { it.name == fromString } ?: FROM.MOVIE
    }

    private val favoriteActions = mapOf(
        FROM.MOVIE to FavoriteAction(
            addFavorite = { id, posterPath, title, releaseDate, voteAverage ->
                detailMovieTvShowViewModel.addMovieToFavorite(
                    id,
                    posterPath,
                    title,
                    releaseDate,
                    voteAverage
                )
            },
            removeFavorite = { id ->
                detailMovieTvShowViewModel.removeMovieFromFavorite(id)
            }
        ),
        FROM.TV_SHOW to FavoriteAction(
            addFavorite = { id, posterPath, title, releaseDate, voteAverage ->
                detailMovieTvShowViewModel.addTvShowFavorite(
                    id,
                    posterPath,
                    title,
                    releaseDate,
                    voteAverage
                )
            },
            removeFavorite = { id ->
                detailMovieTvShowViewModel.removeTvShowFromFavorite(id)
            }
        )
    )

    private val backPressedCallback: OnBackPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            this@DetailMovieTvShowBindingFragment.popBackStack()
        }
    }

    private var idMovieTvShow = 0
    private var title = ""
    private var isChecked = false

    override fun inflateBinding(
        inflater: LayoutInflater, container: ViewGroup?
    ): FragmentDetailMovieTvShowBinding =
        FragmentDetailMovieTvShowBinding.inflate(inflater, container, false)

    override fun onFragmentReady(savedInstanceState: Bundle?) {
        applySystemBarBottomPadding()

        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner, backPressedCallback
        )
        getBundleData()
        showToolbarData()
        setDetailObserver(idMovieTvShow)
        setCastAdapter()
        setCastObserver(idMovieTvShow)
        setTrailerVideoAdapter()
        showTrailerVideoObserver(idMovieTvShow)
        setReviewsAdapter()
        showReviewsObserver(idMovieTvShow)
        setSimilarMovieAdapter()
        showSimilarMovieOrTvShow(idMovieTvShow)
    }

    private fun getBundleData() {
        idMovieTvShow = arguments?.getInt(EXTRA_ID, 0) ?: 0
        title = arguments?.getString(EXTRA_TITLE).orEmpty()
    }

    private fun showToolbarData() {
        binding.apply {
            toolbarDetailMovie.apply {
                tvToolbar.text = title
                imgBack.setOnClickListener {
                    this@DetailMovieTvShowBindingFragment.popBackStack()
                }
            }
        }
    }

    private fun setDetailObserver(id: Int) {
        binding.apply {
            detailMovieTvShowViewModel.apply {
                when (from) {
                    FROM.MOVIE -> {
                        setDetailMovies(id)
                        detailMoviesResponse.observeNetworkResult(
                            lifecycleOwner = viewLifecycleOwner,
                            onLoading = {
                                updateDetailInformationState(isLoading = true)
                            },
                            onSuccess = { response ->
                                val hasOverview = response.overview.isNotEmpty()
                                updateDetailInformationState(isLoading = false, isShowEmpty = !hasOverview)

                                setReadMore()
                                showDetailData(MovieDetailWrapper(response))
                            },
                            onError = {
                                // TODO: create a layout to display error information and binding information on it
                            }
                        )
                    }

                    FROM.TV_SHOW -> {
                        setDetailTvShow(id)
                        detailTvShowResponse.observeNetworkResult(
                            lifecycleOwner = viewLifecycleOwner,
                            onLoading = {
                                updateDetailInformationState(isLoading = true)
                            },
                            onSuccess = { response ->
                                val hasOverview = response.overview.isNotEmpty()
                                updateDetailInformationState(isLoading = false, isShowEmpty = !hasOverview)

                                setReadMore()
                                showDetailData(TvShowDetailWrapper(response))
                            },
                            onError = {
                                // TODO: create a layout to display error information and binding information on it
                            }
                        )
                    }
                }
            }
        }
    }

    private fun showDetailData(data: BaseDetailData) {
        binding.apply {
            // Poster
            imgPoster.loadImage(data.posterPath.orEmpty())

            // Backdrop
            imgBackdrop.loadImage(data.backdropPath)

            // Title
            tvTitle.text = data.titleText.ifEmpty { getString(R.string.tvNA) }

            // Release/Start Date
            val formattedDate = if (data.releaseDateText.isEmpty()) {
                getString(R.string.tvNA)
            } else {
                data.releaseDateText.convertDate(
                    CommonDateFormatConstants.YYYY_MM_DD_FORMAT,
                    CommonDateFormatConstants.EEE_D_MMM_YYYY_FORMAT
                )
            }

            when (from) {
                FROM.MOVIE -> {
                    tvStartedOn.toggleVisibilityIf(from == FROM.TV_SHOW)
                    tvReleasedOn.apply {
                        toggleVisibilityIf(from == FROM.MOVIE)
                        stringFormat(getString(R.string.tvReleasedOn), formattedDate)
                    }
                }

                FROM.TV_SHOW -> {
                    tvReleasedOn.toggleVisibilityIf(from == FROM.MOVIE)
                    tvStartedOn.apply {
                        toggleVisibilityIf(from == FROM.TV_SHOW)
                        stringFormat(getString(R.string.tvStartedOn), formattedDate)
                    }
                }
            }

            // Release Status
            val releaseStatus =
                if (data.statusText.isEmpty()) getString(R.string.tvNA) else data.statusText

            tvReleaseStatus.apply {
                stringReleaseFormat(getString(R.string.tvStatus), releaseStatus)
                toggleVisibilityIf(true)
            }

            // Rating
            val hasRating = data.voteAverage != 0.0
            icTxtRating.apply {
                setIcon(R.drawable.ic_outline_rate)
                requireContext().setIconColor(R.color.colorTextOther)
                setText(
                    if (hasRating) {
                        getString(
                            R.string.tvRatingDesc,
                            data.voteAverage.toRating(),
                            data.voteCount.toString()
                        )
                    } else {
                        getString(
                            R.string.tvRatingDesc,
                            getString(R.string.tvZero),
                            getString(R.string.tvZero)
                        )
                    }
                )
                setTextSize(TEXT_SIZE)
                requireContext().setTextColor(R.color.colorTextOther)
            }

            // Runtime or Episodes
            when (from) {
                FROM.MOVIE -> {
                    val movie = (data as MovieDetailWrapper).movie
                    icTxtRuntime.apply {
                        toggleVisibilityIf(from == FROM.MOVIE)
                        setIcon(R.drawable.ic_outline_runtime)
                        requireContext().setIconColor(R.color.colorTextOther)
                        setText(
                            if (movie.runtime == 0) convertRuntime(0)
                            else convertRuntime(movie.runtime)
                        )
                        setTextSize(TEXT_SIZE)
                        requireContext().setTextColor(R.color.colorTextOther)
                    }
                }

                FROM.TV_SHOW -> {
                    val tvShow = (data as TvShowDetailWrapper).tvShow
                    icTxtEpisodes.apply {
                        toggleVisibilityIf(from == FROM.TV_SHOW)
                        setIcon(R.drawable.ic_outline_episode)
                        requireContext().setIconColor(R.color.colorTextOther)
                        setText(
                            if (tvShow.numberOfEpisodes == 0) getString(R.string.tvNA)
                            else getString(
                                R.string.tvEpsDesc,
                                tvShow.numberOfEpisodes.toString(),
                                getString(R.string.tvEps)
                            )
                        )
                        setTextSize(TEXT_SIZE)
                        requireContext().setTextColor(R.color.colorTextOther)
                    }

                    val networkText = when {
                        tvShow.networks.isEmpty() -> getString(R.string.tvNA)
                        tvShow.networks.first().originCountry.isEmpty() -> getString(
                            R.string.tvNetworkDesc,
                            tvShow.networks.first().name,
                            getString(R.string.tvNA)
                        )

                        else -> getString(
                            R.string.tvNetworkDesc,
                            tvShow.networks.first().name,
                            tvShow.networks.first().originCountry
                        )
                    }
                    tvNetwork.text = networkText
                }
            }

            // Age Rating
            icTxtAgeRating.apply {
                setIcon(if (data.isAdult) R.drawable.ic_outline_adult else R.drawable.ic_outline_no_adult)
                requireContext().setIconColor(R.color.colorTextOther)
                setText(if (data.isAdult) getString(R.string.tvAdults) else getString(R.string.tvAllAges))
                setTextSize(TEXT_SIZE)
                requireContext().setTextColor(R.color.colorTextOther)
            }

            // Language
            icTxtLanguage.apply {
                setIcon(R.drawable.ic_outline_spoken_language)
                requireContext().setIconColor(R.color.colorTextOther)
                setText(
                    if (data.spokenLanguages.isEmpty()) getString(R.string.tvNA)
                    else data.spokenLanguages.joinToString(", ")
                )
                setTextSize(TEXT_SIZE)
                requireContext().setTextColor(R.color.colorTextOther)
            }

            // Genres
            icTxtGenre.apply {
                setIcon(R.drawable.ic_outline_genre)
                requireContext().setIconColor(R.color.colorTextOther)
                setText(
                    if (data.genres.isEmpty()) getString(R.string.tvNA)
                    else data.genres.joinToString()
                )
                setTextSize(TEXT_SIZE)
                requireContext().setTextColor(R.color.colorTextOther)
            }

            // Overview
            val hasOverview = data.overview.isNotEmpty()
            tvOverview.apply {
                text = data.overview
                toggleVisibilityIf(hasOverview)
            }
            tvNoOverview.toggleVisibilityIf(!hasOverview)
            tvReadMore.toggleVisibilityIf(hasOverview)

            // Action: Favorite
            checkFavoriteData(data.id)
            setActionFavorite(
                data.id,
                data.posterPath.orEmpty(),
                data.titleText,
                data.releaseDateText,
                data.voteAverage
            )

            // Action: Share
            if (from == FROM.MOVIE) shareAwesomeMovieOrTvShow(data.homepage.orEmpty())
            else shareAwesomeMovieOrTvShow(data.homepage.orEmpty())
        }
    }

    private fun checkFavoriteData(id: Int) {
        binding.apply {
            lifecycleScope.launch {
                var count = 0
                count = when (from) {
                    FROM.MOVIE -> detailMovieTvShowViewModel.checkFavoriteMovie(id)
                    FROM.TV_SHOW -> detailMovieTvShowViewModel.checkFavoriteTvShow(id)
                }
                toolbarDetailMovie.toggleFavorite.isChecked = count > 0
                isChecked = count > 0
            }
        }
    }

    private fun setActionFavorite(
        id: Int,
        posterPath: String,
        title: String,
        releaseDate: String,
        voteAverage: Double
    ) {
        binding.apply {
            toolbarDetailMovie.toggleFavorite.setOnClickListener {
                isChecked = !isChecked

                val action = favoriteActions[from] ?: return@setOnClickListener

                if (isChecked) {
                    action.addFavorite(id, posterPath, title, releaseDate, voteAverage)
                    requireContext().showSuccessToastyIcon(getString(R.string.action_added_to_favorite))
                } else {
                    action.removeFavorite(id)
                    requireContext().showSuccessToastyIcon(getString(R.string.action_removed_from_favorite))
                }
            }
        }
    }

    private fun shareAwesomeMovieOrTvShow(link: String) {
        binding.toolbarDetailMovie.imgShare.setOnClickListener {
            requireContext().share(
                getString(
                    when (from) {
                        FROM.MOVIE -> R.string.tvVisitMovie
                        FROM.TV_SHOW -> R.string.tvVisitTvShow
                    }
                ),
                link
            )
        }
    }

    private fun setReadMore() {
        binding.apply {
            tvReadMore.toggleVisibilityIf(true)
            tvReadMore.setOnClickListener {
                if (tvReadMore.text.toString() == getString(R.string.tvReadMore)) {
                    tvOverview.maxLines = Integer.MAX_VALUE
                    tvOverview.ellipsize = null
                    tvReadMore.text = getString(R.string.tvReadLess)
                } else {
                    tvOverview.maxLines = 4
                    tvOverview.ellipsize = TextUtils.TruncateAt.END
                    tvReadMore.text = getString(R.string.tvReadMore)
                }
            }
        }
    }

    private fun setCastAdapter() {
        binding.rvMovieCast.apply {
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            clipToPadding = false
            clipChildren = false
            setHasFixedSize(false)
            adapter = castAdapter

            val snapHelper = DynamicSnapHelper()
            snapHelper.attachToRecyclerView(this)
        }
    }

    private fun setCastObserver(id: Int) {
        binding.apply {
            detailMovieTvShowViewModel.apply {
                when (from) {
                    FROM.MOVIE -> {
                        setDetailMovieCast(id)
                        detailMoviesCastResponse.observeNetworkResult(
                            lifecycleOwner = viewLifecycleOwner,
                            onLoading = {
                                updateCastSectionState(isLoading = true)
                            },
                            onSuccess = { response ->
                                val results = response.cast
                                val isResultsEmpty = results.isEmpty()
                                updateCastSectionState(isLoading = false, isShowEmpty = isResultsEmpty)

                                if (isResultsEmpty) return@observeNetworkResult

                                val castList = response.cast.map {
                                    BaseCastItemImpl(
                                        id = it.id,
                                        name = it.name,
                                        character = it.character,
                                        profilePath = it.profilePath.orEmpty()
                                    )
                                }
                                castAdapter.submitList(castList)
                            },
                            onError = {
                                updateCastSectionState(isLoading = false, isShowEmpty = false)

                                // TODO: create a layout to display error information and binding information on it
                            }
                        )
                    }

                    FROM.TV_SHOW -> {
                        setDetailTvShowsCast(id)
                        detailTvShowCastResponse.observeNetworkResult(
                            lifecycleOwner = viewLifecycleOwner,
                            onLoading = {
                                updateCastSectionState(isLoading = true)
                            },
                            onSuccess = { response ->
                                val results = response.cast
                                val isResultsEmpty = results.isEmpty()
                                updateCastSectionState(isLoading = false, isShowEmpty = isResultsEmpty)

                                if (isResultsEmpty) return@observeNetworkResult

                                val castList = response.cast.map {
                                    BaseCastItemImpl(
                                        id = it.id,
                                        name = it.name,
                                        character = it.character,
                                        profilePath = it.profilePath.orEmpty()
                                    )
                                }
                                castAdapter.submitList(castList)
                            },
                            onError = {
                                updateCastSectionState(isLoading = false, isShowEmpty = false)

                                // TODO: create a layout to display error information and binding information on it
                            }
                        )
                    }
                }
            }
        }
    }

    private fun setTrailerVideoAdapter() {
        trailerVideoAdapter = MovieTvShowTrailerVideoAdapter {
            requireContext().showTrailerVideo(it.key)
        }

        binding.rvTrailerVideo.apply {
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            clipToPadding = false
            clipChildren = false
            setHasFixedSize(true)
            adapter = trailerVideoAdapter

            val snapHelper = DynamicSnapHelper()
            snapHelper.attachToRecyclerView(this)
        }
    }

    private fun showTrailerVideoObserver(id: Int) {
        binding.apply {
            detailMovieTvShowViewModel.apply {
                when (from) {
                    FROM.MOVIE -> {
                        setDetailMovieVideo(id)
                        detailMoviesVideoResponse.observeNetworkResult(
                            lifecycleOwner = viewLifecycleOwner,
                            onLoading = {
                                updateTrailerVideoSectionState(isLoading = true)
                            },
                            onSuccess = { response ->
                                val results = response.results
                                val isEmptyResult = results.isEmpty()
                                updateTrailerVideoSectionState(isLoading = false, isShowEmpty = isEmptyResult)

                                if (isEmptyResult) return@observeNetworkResult

                                val mappedList = results.toMovieBaseVideoItemList()
                                trailerVideoAdapter?.submitList(mappedList)
                            },
                            onError = {
                                updateTrailerVideoSectionState(isLoading = false, isShowEmpty = false)

                                // TODO: create a layout to display error information and binding information on it
                            }
                        )
                    }

                    FROM.TV_SHOW -> {
                        setDetailTvShowVideo(id)
                        detailTvShowVideoResponse.observeNetworkResult(
                            lifecycleOwner = viewLifecycleOwner,
                            onLoading = {
                                updateTrailerVideoSectionState(isLoading = true)
                            },
                            onSuccess = { response ->
                                val results = response.results
                                val isEmptyResult = results.isEmpty()
                                updateTrailerVideoSectionState(isLoading = false, isShowEmpty = isEmptyResult)

                                if (isEmptyResult) return@observeNetworkResult

                                val mappedList = response.results.toTvShowBaseVideoItemList()
                                trailerVideoAdapter?.submitList(mappedList)
                            },
                            onError = {
                                updateTrailerVideoSectionState(isLoading = false, isShowEmpty = false)

                                // TODO: create a layout to display error information and binding information on it
                            }
                        )
                    }
                }
            }
        }
    }

    private fun setReviewsAdapter() {
        binding.rvMovieReviews.apply {
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            setHasFixedSize(true)
            adapter = reviewsAdapter

            val helper: SnapHelper = LinearSnapHelper()
            helper.attachToRecyclerView(this)
        }
    }

    private fun showReviewsObserver(id: Int) {
        binding.apply {
            detailMovieTvShowViewModel.apply {
                when (from) {
                    FROM.MOVIE -> {
                        setDetailMovieReviews(id)
                        detailMovieReviewsResponse.observeNetworkResult(
                            lifecycleOwner = viewLifecycleOwner,
                            onLoading = {
                                updateReviewsSectionState(isLoading = true)
                            },
                            onSuccess = { response ->
                                val results = response.results
                                val isResultsEmpty = results.isEmpty()
                                updateReviewsSectionState(isLoading = false, isShowEmpty = isResultsEmpty)

                                if (isResultsEmpty) return@observeNetworkResult

                                reviewsAdapter.submitList(results)
                            },
                            onError = {
                                updateReviewsSectionState(isLoading = false, isShowEmpty = false)

                                // TODO: create a layout to display error information and binding information on it
                            }
                        )
                    }

                    FROM.TV_SHOW -> {
                        setDetailTvShowsReviews(id)
                        detailTvShowReviewsResponse.observeNetworkResult(
                            lifecycleOwner = viewLifecycleOwner,
                            onLoading = {
                                updateReviewsSectionState(isLoading = true)
                            },
                            onSuccess = { response ->
                                val results = response.results
                                val isResultsEmpty = results.isEmpty()
                                updateReviewsSectionState(isLoading = false, isShowEmpty = isResultsEmpty)

                                if (isResultsEmpty) return@observeNetworkResult

                                reviewsAdapter.submitList(results)
                            },
                            onError = {
                                updateReviewsSectionState(isLoading = false, isShowEmpty = false)

                                // TODO: create a layout to display error information and binding information on it
                            }
                        )
                    }
                }
            }
        }
    }

    private fun setSimilarMovieAdapter() {
        similarAdapter = MovieTvShowSimilarAdapter {
            navigateToDetailMovieTvShow(
                id = it.id,
                title = it.titleText,
                from = from
            )
        }

        binding.rvMovieSimilar.apply {
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            clipToPadding = false
            clipChildren = false
            setHasFixedSize(false)
            adapter = similarAdapter

            val snapHelper = DynamicSnapHelper()
            snapHelper.attachToRecyclerView(this)
        }
    }

    private fun showSimilarMovieOrTvShow(id: Int) {
        binding.apply {
            detailMovieTvShowViewModel.apply {
                when (from) {
                    FROM.MOVIE -> {
                        setDetailMovieSimilar(id)
                        detailMovieSimilarResponse.observeNetworkResult(
                            lifecycleOwner = viewLifecycleOwner,
                            onLoading = {
                                updateSimilarSectionState(isLoading = true)
                            },
                            onSuccess = { response ->
                                val results = response.results
                                val isResultsEmpty = results.isEmpty()
                                updateSimilarSectionState(isLoading = false, isShowEmpty = isResultsEmpty)

                                if (isResultsEmpty) return@observeNetworkResult

                                val mappedList = results.map {
                                    BaseSimilarItemImpl(
                                        id = it.id,
                                        posterPath = it.posterPath.orEmpty(),
                                        titleText = it.originalTitle,
                                        releaseDateText = it.releaseDate
                                    )
                                }
                                similarAdapter?.submitList(mappedList)
                            },
                            onError = {
                                updateSimilarSectionState(isLoading = false, isShowEmpty = false)

                                // TODO: create a layout to display error information and binding information on it
                            }
                        )
                    }

                    FROM.TV_SHOW -> {
                        setDetailTvShowsSimilar(id)
                        detailTvShowSimilarResponse.observeNetworkResult(
                            lifecycleOwner = viewLifecycleOwner,
                            onLoading = {
                                updateSimilarSectionState(isLoading = true)
                            },
                            onSuccess = { response ->
                                val results = response.results
                                val isResultsEmpty = results.isEmpty()
                                updateSimilarSectionState(isLoading = false, isShowEmpty = isResultsEmpty)

                                if (isResultsEmpty) return@observeNetworkResult

                                val mappedList = results.map {
                                    BaseSimilarItemImpl(
                                        id = it.id,
                                        posterPath = it.posterPath.orEmpty(),
                                        titleText = it.originalName,
                                        releaseDateText = it.firstAirDate
                                    )
                                }
                                similarAdapter?.submitList(mappedList)
                            },
                            onError = {
                                updateSimilarSectionState(isLoading = false, isShowEmpty = false)

                                // TODO: create a layout to display error information and binding information on it
                            }
                        )
                    }
                }
            }
        }
    }

    private fun convertRuntime(data: Int): String {
        val hours = data / TIME_60
        val minutes = data % TIME_60
        return getString(R.string.tvRuntimeInTime, hours, minutes)
    }

    private fun updateDetailInformationState(
        isLoading: Boolean = false,
        isShowEmpty: Boolean = false
    ) {
        binding.apply {
            if (isLoading) {
                groupDetailInformation.hideView()
                return
            }

            groupDetailInformation.showView()

            tvOverviewTitle.toggleVisibilityIf(!isShowEmpty)
            tvOverview.toggleVisibilityIf(!isShowEmpty)
            tvReadMore.toggleVisibilityIf(!isShowEmpty)
            tvNoOverview.toggleVisibilityIf(isShowEmpty)
        }
    }

    private fun updateCastSectionState(
        isLoading: Boolean = false,
        isShowEmpty: Boolean = false
    ) {
        binding.apply {
            if (isLoading) {
                groupCast.hideView()
                rvMovieCast.hideView()
                tvNoCast.hideView()
                return
            }

            groupCast.showView()
            tvCast.showView()

            tvNoCast.toggleVisibilityIf(isShowEmpty)
            rvMovieCast.toggleVisibilityIf(!isShowEmpty)
        }
    }

    private fun updateTrailerVideoSectionState(
        isLoading: Boolean = false,
        isShowEmpty: Boolean = false
    ) {
        binding.apply {
            if (isLoading) {
                groupTrailerVideo.hideView()
                rvTrailerVideo.hideView()
                tvNoVideo.hideView()
                return
            }

            groupTrailerVideo.showView()
            tvTrailer.showView()

            tvNoVideo.toggleVisibilityIf(isShowEmpty)
            rvTrailerVideo.toggleVisibilityIf(!isShowEmpty)
        }
    }

    private fun updateReviewsSectionState(
        isLoading: Boolean = false,
        isShowEmpty: Boolean = false
    ) {
        binding.apply {
            if (isLoading) {
                groupReviews.hideView()
                rvMovieReviews.hideView()
                tvNoReviews.hideView()
                return
            }

            groupReviews.showView()
            tvReviewsTitle.showView()

            tvNoReviews.toggleVisibilityIf(isShowEmpty)
            rvMovieReviews.toggleVisibilityIf(!isShowEmpty)
        }
    }

    private fun updateSimilarSectionState(
        isLoading: Boolean = false,
        isShowEmpty: Boolean = false
    ) {
        binding.apply {
            if (isLoading) {
                groupSimilar.hideView()
                rvMovieSimilar.hideView()
                tvNoSimilar.hideView()
                return
            }

            groupSimilar.showView()
            tvMovieSimilar.toggleVisibilityIf(from == FROM.MOVIE)
            tvTvShowSimilar.toggleVisibilityIf(from == FROM.TV_SHOW)

            tvNoSimilar.toggleVisibilityIf(isShowEmpty)
            rvMovieSimilar.toggleVisibilityIf(!isShowEmpty)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        similarAdapter = null
    }

    companion object {
        private const val TEXT_SIZE = 12f
        const val EXTRA_ID = "EXTRA_ID"
        const val EXTRA_TITLE = "EXTRA_TITLE"
        const val EXTRA_FROM = "EXTRA_FROM"
        const val TIME_60 = 60

        enum class FROM {
            MOVIE, TV_SHOW
        }
    }
}