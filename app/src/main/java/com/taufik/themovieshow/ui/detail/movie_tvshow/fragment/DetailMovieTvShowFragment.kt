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
import com.taufik.themovieshow.base.helper.BaseVideoItem
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
                                groupDetailInformation.hideView()
                            },
                            onSuccess = { response ->
                                groupDetailInformation.showView()
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
                                groupDetailInformation.hideView()
                            },
                            onSuccess = { response ->
                                groupDetailInformation.showView()
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
            val helper: SnapHelper = LinearSnapHelper()
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            helper.attachToRecyclerView(this)
            setHasFixedSize(true)
            adapter = castAdapter
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
                                groupCast.hideView()
                            },
                            onSuccess = { response ->
                                groupCast.showView()

                                val castList = response.cast.map {
                                    BaseCastItemImpl(
                                        id = it.id,
                                        name = it.name,
                                        character = it.character,
                                        profilePath = it.profilePath.orEmpty()
                                    )
                                }
                                handleCastResponse(castList)
                            },
                            onError = {
                                tvNoCast.hideView()

                                // TODO: create a layout to display error information and binding information on it
                            }
                        )
                    }

                    FROM.TV_SHOW -> {
                        setDetailTvShowsCast(id)
                        detailTvShowCastResponse.observeNetworkResult(
                            lifecycleOwner = viewLifecycleOwner,
                            onLoading = {
                                tvNoCast.hideView()
                            },
                            onSuccess = { response ->
                                val castList = response.cast.map {
                                    BaseCastItemImpl(
                                        id = it.id,
                                        name = it.name,
                                        character = it.character,
                                        profilePath = it.profilePath.orEmpty()
                                    )
                                }
                                handleCastResponse(castList)
                            },
                            onError = {
                                tvNoCast.hideView()
                            }
                        )
                    }
                }
            }
        }
    }

    private fun handleCastResponse(castList: List<BaseCastItemImpl>?) {
        if (castList.isNullOrEmpty()) {
            binding.tvNoCast.showView()
            return
        }

        binding.tvNoCast.hideView()
        castAdapter.submitList(castList)
    }


    private fun setTrailerVideoAdapter() {
        trailerVideoAdapter = MovieTvShowTrailerVideoAdapter {
            requireContext().showTrailerVideo(it.key)
        }

        binding.rvTrailerVideo.apply {
            val helper: SnapHelper = LinearSnapHelper()
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            helper.attachToRecyclerView(this)
            setHasFixedSize(true)
            adapter = trailerVideoAdapter
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
                                groupTrailerVideo.hideView()
                                showVideo(false)
                            },
                            onSuccess = { response ->
                                groupTrailerVideo.showView()

                                val mappedList = response.results.toMovieBaseVideoItemList()
                                handleTrailerVideoResponse(mappedList)
                            },
                            onError = {
                                showVideo(false)

                                // TODO: create a layout to display error information and binding information on it
                            }
                        )
                    }

                    FROM.TV_SHOW -> {
                        setDetailTvShowVideo(id)
                        detailTvShowVideoResponse.observeNetworkResult(
                            lifecycleOwner = viewLifecycleOwner,
                            onLoading = {
                                groupTrailerVideo.hideView()
                                showVideo(false)
                            },
                            onSuccess = { response ->
                                groupTrailerVideo.showView()

                                val mappedList = response.results.toTvShowBaseVideoItemList()
                                handleTrailerVideoResponse(mappedList)
                            },
                            onError = {
                                showVideo(false)

                                // TODO: create a layout to display error information and binding information on it
                            }
                        )
                    }
                }
            }
        }
    }

    private fun handleTrailerVideoResponse(mappedList: List<BaseVideoItem>) {
        if (mappedList.isEmpty()) {
            showVideo(false)
        } else {
            showVideo(true)
            trailerVideoAdapter?.submitList(mappedList)
        }
    }

    private fun setReviewsAdapter() {
        binding.rvMovieReviews.apply {
            val helper: SnapHelper = LinearSnapHelper()
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            helper.attachToRecyclerView(this)
            setHasFixedSize(true)
            adapter = reviewsAdapter
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
                                groupReviews.hideView()
                            },
                            onSuccess = { response ->
                                groupReviews.showView()
                                tvNoReviews.hideView()

                                val results = response.results
                                if (results.isEmpty()) {
                                    tvNoReviews.showView()
                                    return@observeNetworkResult
                                }
                                reviewsAdapter.submitList(results)
                            },
                            onError = {
                                tvNoReviews.hideView()

                                // TODO: create a layout to display error information and binding information on it
                            }
                        )
                    }

                    FROM.TV_SHOW -> {
                        setDetailTvShowsReviews(id)
                        detailTvShowReviewsResponse.observeNetworkResult(
                            lifecycleOwner = viewLifecycleOwner,
                            onLoading = {
                                groupReviews.hideView()
                            },
                            onSuccess = { response ->
                                groupReviews.showView()
                                tvNoReviews.hideView()

                                val results = response.results
                                if (results.isEmpty()) {
                                    tvNoReviews.showView()
                                    return@observeNetworkResult
                                }
                                reviewsAdapter.submitList(results)
                            },
                            onError = {
                                tvNoReviews.hideView()

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
            val helper: SnapHelper = LinearSnapHelper()
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            helper.attachToRecyclerView(this)
            setHasFixedSize(true)
            adapter = similarAdapter
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
                                groupSimilar.hideView()
                            },
                            onSuccess = { response ->
                                groupSimilar.showView()
                                showNoSimilarMovie(false)
                                tvMovieSimilar.toggleVisibilityIf(from == FROM.MOVIE)

                                val results = response.results
                                if (results.isEmpty()) {
                                    showNoSimilarMovie(true)
                                    return@observeNetworkResult
                                }

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
                                showNoSimilarMovie(false)

                                // TODO: create a layout to display error information and binding information on it
                            }
                        )
                    }

                    FROM.TV_SHOW -> {
                        setDetailTvShowsSimilar(id)
                        detailTvShowSimilarResponse.observeNetworkResult(
                            lifecycleOwner = viewLifecycleOwner,
                            onLoading = {
                                groupSimilar.hideView()
                            },
                            onSuccess = { response ->
                                groupSimilar.showView()
                                showNoSimilarTvShow(false)
                                tvTvShowSimilar.toggleVisibilityIf(from == FROM.TV_SHOW)

                                val results = response.results
                                if (results.isEmpty()) {
                                    showNoSimilarTvShow(true)
                                    return@observeNetworkResult
                                }

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
                                showNoSimilarTvShow(false)

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

    private fun showVideo(isShow: Boolean) {
        binding.apply {
            rvTrailerVideo.toggleVisibilityIf(isShow)
            tvNoVideo.toggleVisibilityIf(!isShow)
        }
    }

    private fun showNoSimilarMovie(isShow: Boolean) {
        binding.apply {
            rvMovieSimilar.toggleVisibilityIf(!isShow)
            tvNoSimilar.toggleVisibilityIf(isShow)
        }
    }

    private fun showNoSimilarTvShow(isShow: Boolean) {
        binding.apply {
            tvTvShowSimilar.toggleVisibilityIf(!isShow)
            tvNoSimilar.toggleVisibilityIf(isShow)
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