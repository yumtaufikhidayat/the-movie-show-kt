package com.taufik.themovieshow.ui.detail.movie_tvshow.fragment

import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.SnapHelper
import com.taufik.themovieshow.R
import com.taufik.themovieshow.base.fragment.BaseFragment
import com.taufik.themovieshow.base.helper.BaseDetailData
import com.taufik.themovieshow.base.helper.MovieDetailWrapper
import com.taufik.themovieshow.base.helper.TvShowDetailWrapper
import com.taufik.themovieshow.base.helper.toBaseReviewItem
import com.taufik.themovieshow.base.helper.toCastItem
import com.taufik.themovieshow.base.helper.toMovieBaseSimilarItem
import com.taufik.themovieshow.base.helper.toTvShowSimilarItem
import com.taufik.themovieshow.databinding.FragmentDetailMovieTvShowBinding
import com.taufik.themovieshow.ui.detail.movie_tvshow.adapter.MovieTvShowCastAdapter
import com.taufik.themovieshow.ui.detail.movie_tvshow.adapter.MovieTvShowReviewsAdapter
import com.taufik.themovieshow.ui.detail.movie_tvshow.adapter.MovieTvShowSimilarAdapter
import com.taufik.themovieshow.ui.detail.movie_tvshow.adapter.MovieTvShowTrailerVideoAdapter
import com.taufik.themovieshow.ui.detail.movie_tvshow.viewmodel.DetailMovieTvShowViewModel
import com.taufik.themovieshow.ui.favorite.helper.FavoriteAction
import com.taufik.themovieshow.utils.enums.DetailTypeEnum
import com.taufik.themovieshow.utils.enums.FROM
import com.taufik.themovieshow.utils.enums.RatingCategory
import com.taufik.themovieshow.utils.extensions.applySystemBarBottomPadding
import com.taufik.themovieshow.utils.extensions.convertDate
import com.taufik.themovieshow.utils.extensions.hideView
import com.taufik.themovieshow.utils.extensions.loadImage
import com.taufik.themovieshow.utils.extensions.loadPosterImage
import com.taufik.themovieshow.utils.extensions.navigateToDetailMovieTvShow
import com.taufik.themovieshow.utils.extensions.observeNetworkResult
import com.taufik.themovieshow.utils.extensions.orNA
import com.taufik.themovieshow.utils.extensions.popBackStack
import com.taufik.themovieshow.utils.extensions.releaseInfo
import com.taufik.themovieshow.utils.extensions.share
import com.taufik.themovieshow.utils.extensions.showError
import com.taufik.themovieshow.utils.extensions.showSnackBar
import com.taufik.themovieshow.utils.extensions.showSuccessToastyIcon
import com.taufik.themovieshow.utils.extensions.showTrailerVideo
import com.taufik.themovieshow.utils.extensions.showView
import com.taufik.themovieshow.utils.extensions.toMovieBaseVideoItemList
import com.taufik.themovieshow.utils.extensions.toRating
import com.taufik.themovieshow.utils.extensions.toThousandFormat
import com.taufik.themovieshow.utils.extensions.toTvShowBaseVideoItemList
import com.taufik.themovieshow.utils.extensions.toggleVisibilityIf
import com.taufik.themovieshow.utils.extensions.tryOpenBrowser
import com.taufik.themovieshow.utils.helper.DynamicSnapHelper
import com.taufik.themovieshow.utils.objects.CommonDateFormatConstants
import com.taufik.themovieshow.utils.objects.getUserCountryCertification
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class DetailMovieTvShowBindingFragment : BaseFragment<FragmentDetailMovieTvShowBinding>() {

    private val detailMovieTvShowViewModel: DetailMovieTvShowViewModel by viewModels()

    private val castAdapter by lazy { MovieTvShowCastAdapter() }
    private val trailerVideoAdapter by lazy {
        MovieTvShowTrailerVideoAdapter {
            if (!isAdded) return@MovieTvShowTrailerVideoAdapter
            context?.showTrailerVideo(it.key)
        }
    }

    private val reviewsAdapter by lazy {
        MovieTvShowReviewsAdapter {
            context?.tryOpenBrowser(it)
        }
    }

    private val similarAdapter by lazy {
        MovieTvShowSimilarAdapter {
            navigateToDetailMovieTvShow(
                id = it.id,
                title = it.titleText,
                from = from
            )
        }
    }

    private val idMovieTvShow by lazy { arguments?.getInt(EXTRA_ID, 0) ?: 0 }
    private val title by lazy { arguments?.getString(EXTRA_TITLE).orEmpty() }

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

    private var isChecked = false

    override fun inflateBinding(
        inflater: LayoutInflater, container: ViewGroup?
    ): FragmentDetailMovieTvShowBinding =
        FragmentDetailMovieTvShowBinding.inflate(inflater, container, false)

    override fun onFragmentReady(savedInstanceState: Bundle?) {
        applySystemBarBottomPadding()

        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            backPressedCallback
        )

        showToolbarData()
        setDetailObserver(idMovieTvShow)
        setCastAdapter()
        setTrailerVideoAdapter()
        setReviewsAdapter()
        setSimilarMovieAdapter()
    }

    private fun showToolbarData() {
        binding.imgBack.setOnClickListener {
            this@DetailMovieTvShowBindingFragment.popBackStack()
        }
    }

    private fun setDetailObserver(id: Int) {
        binding.apply {
            detailMovieTvShowViewModel.apply {
                when (from) {
                    FROM.MOVIE -> {
                        detailMoviesResponse(id).observeNetworkResult(
                            lifecycleOwner = viewLifecycleOwner,
                            onLoading = {
                                showShimmer(isLoading = true, type = DetailTypeEnum.OVERVIEW)
                                updateDetailInformationState(isLoading = true)
                            },
                            onSuccess = { response ->
                                showShimmer(isLoading = false, type = DetailTypeEnum.OVERVIEW)

                                val hasOverview = response.overview.isNotEmpty()
                                updateDetailInformationState(
                                    isLoading = false,
                                    isShowEmpty = !hasOverview
                                )

                                setReadMore()
                                showDetailData(MovieDetailWrapper(response))

                                setCastObserver(id)
                            },
                            onError = { errorMessage ->
                                showShimmer(isLoading = false, type = DetailTypeEnum.OVERVIEW)
                                updateDetailInformationState(isLoading = false, isShowEmpty = false)

                                layoutError.showError(
                                    context = requireContext(),
                                    message = errorMessage,
                                    onRetry = {
                                        // TODO: Reload data
                                    }
                                )
                            }
                        )
                    }

                    FROM.TV_SHOW -> {
                        detailTvShowResponse(id).observeNetworkResult(
                            lifecycleOwner = viewLifecycleOwner,
                            onLoading = {
                                showShimmer(isLoading = true, type = DetailTypeEnum.OVERVIEW)
                                updateDetailInformationState(isLoading = true)
                            },
                            onSuccess = { response ->
                                showShimmer(isLoading = false, type = DetailTypeEnum.OVERVIEW)

                                val hasOverview = response.overview.isNotEmpty()
                                updateDetailInformationState(
                                    isLoading = false,
                                    isShowEmpty = !hasOverview
                                )

                                setReadMore()
                                showDetailData(TvShowDetailWrapper(response))

                                setCastObserver(id)
                            },
                            onError = {
                                showShimmer(isLoading = false, type = DetailTypeEnum.OVERVIEW)
                                updateDetailInformationState(isLoading = false, isShowEmpty = false)
                                layoutError.showError(
                                    context = requireContext(),
                                    message = it,
                                    onRetry = {
                                        // TODO: Reload data
                                    }
                                )
                            }
                        )
                    }
                }
            }
        }
    }

    private fun showDetailData(data: BaseDetailData) {
        if (!isAdded) return

        binding.apply {
            // Poster
            imgPoster.loadPosterImage(data.posterPath.orEmpty())

            // Backdrop
            imgBackdrop.loadImage(data.backdropPath)

            // Title
            tvTitle.text = data.titleText.orNA(requireContext())

            // Release/Start Date
            val formattedDate = data.releaseDateText
                .takeIf { it.isNotEmpty() }
                ?.convertDate(
                    inputFormat = CommonDateFormatConstants.YYYY_MM_DD_FORMAT,
                    outputFormat = CommonDateFormatConstants.EEE_D_MMM_YYYY_FORMAT
                ) ?: getString(R.string.tvNA)

            // Release Status
            val releaseStatus = data.statusText.orNA(requireContext())
            tvReleaseInfo.apply {
                toggleVisibilityIf(from == FROM.MOVIE || from == FROM.TV_SHOW)
                releaseInfo(formattedDate, releaseStatus)
            }

            // Rating
            val hasRating = data.voteAverage != 0.0
            val voteCount = data.voteCount.toThousandFormat()

            icTxtRating.apply {
                setIcon(R.drawable.ic_outline_rate)
                context?.setIconColor(R.color.colorTextOther)
                setText(
                    if (hasRating) {
                        getString(
                            R.string.tvRatingDesc,
                            data.voteAverage.toRating(),
                            voteCount
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
                context?.setTextColor(R.color.colorTextOther)
            }

            // Runtime or Episodes
            when (from) {
                FROM.MOVIE -> {
                    val movie = (data as MovieDetailWrapper).movie
                    icTxtRuntime.apply {
                        toggleVisibilityIf(from == FROM.MOVIE)
                        icTxtEpisodes.hideView()

                        setIcon(R.drawable.ic_outline_runtime)
                        context?.setIconColor(R.color.colorTextOther)
                        setText(
                            if (movie.runtime <= 0) 0.convertRuntime()
                            else movie.runtime.convertRuntime()
                        )
                        setTextSize(TEXT_SIZE)
                        context?.setTextColor(R.color.colorTextOther)
                    }
                    tvNetwork.toggleVisibilityIf(from == FROM.TV_SHOW)
                }

                FROM.TV_SHOW -> {
                    val tvShow = (data as TvShowDetailWrapper).tvShow
                    icTxtEpisodes.apply {
                        toggleVisibilityIf(from == FROM.TV_SHOW)
                        icTxtRuntime.hideView()

                        setIcon(R.drawable.ic_outline_episode)
                        context?.setIconColor(R.color.colorTextOther)
                        setText(
                            if (tvShow.numberOfEpisodes == 0) getString(R.string.tvNA)
                            else getString(
                                R.string.tvEpsDesc,
                                tvShow.numberOfEpisodes.toString(),
                                getString(R.string.tvEps)
                            )
                        )
                        setTextSize(TEXT_SIZE)
                        context?.setTextColor(R.color.colorTextOther)
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
            // If age is adult, show adult confirmation dialog
            val ageCertCode = data.getUserCountryCertification()
            val category = RatingCategory.fromCode(ageCertCode)
            val labelResId = category.labelResId
            val labelIcon = when (labelResId) {
                R.string.tvAllAges -> R.drawable.ic_outline_no_adult
                R.string.tvTeens -> R.drawable.ic_outline_face_teen
                R.string.tvAdults -> R.drawable.ic_outline_adult
                else -> R.drawable.ic_outline_no_adult_content
            }

            icTxtAgeRating.apply {
                setIcon(labelIcon)
                context?.setIconColor(R.color.colorTextOther)
                setText(getString(labelResId))
                setTextSize(TEXT_SIZE)
                context?.setTextColor(R.color.colorTextOther)
            }

            // Language
            icTxtLanguage.apply {
                setIcon(R.drawable.ic_outline_spoken_language)
                context?.setIconColor(R.color.colorTextOther)
                setText(data.spokenLanguages.orNA(requireContext()))
                setTextSize(TEXT_SIZE)
                context?.setTextColor(R.color.colorTextOther)
            }

            // Genres
            icTxtGenre.apply {
                setIcon(R.drawable.ic_outline_genre)
                context?.setIconColor(R.color.colorTextOther)
                setText(data.genres.orNA(requireContext()))
                setTextSize(TEXT_SIZE)
                context?.setTextColor(R.color.colorTextOther)
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
            viewLifecycleOwner.lifecycleScope.launch {
                viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                    val count = when (from) {
                        FROM.MOVIE -> detailMovieTvShowViewModel.checkFavoriteMovie(id)
                        FROM.TV_SHOW -> detailMovieTvShowViewModel.checkFavoriteTvShow(id)
                    }
                    toggleFavorite.isChecked = count > 0
                    isChecked = count > 0
                }
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
        if (!isAdded) return
        binding.apply {
            toggleFavorite.setOnClickListener {
                isChecked = !isChecked

                val action = favoriteActions[from] ?: return@setOnClickListener

                if (isChecked) {
                    action.addFavorite(id, posterPath, title, releaseDate, voteAverage)
                    context?.showSuccessToastyIcon(getString(R.string.action_added_to_favorite))
                } else {
                    action.removeFavorite(id)
                    context?.showSuccessToastyIcon(getString(R.string.action_removed_from_favorite))
                }
            }
        }
    }

    private fun shareAwesomeMovieOrTvShow(link: String) {
        if (!isAdded) return
        binding.imgShare.setOnClickListener {
            context?.share(
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
        binding.rvCast.apply {
            layoutManager = LinearLayoutManager(
                requireContext(),
                LinearLayoutManager.HORIZONTAL,
                false
            )
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
                        detailMoviesCastResponse(id).observeNetworkResult(
                            lifecycleOwner = viewLifecycleOwner,
                            onLoading = {
                                showShimmer(isLoading = true, type = DetailTypeEnum.CAST)
                                updateCastSectionState(isLoading = true)
                            },
                            onSuccess = { response ->

                                val results = response.cast
                                val isResultsEmpty = results.isEmpty()

                                showShimmer(isLoading = false, type = DetailTypeEnum.CAST)
                                updateCastSectionState(
                                    isLoading = false,
                                    isShowEmpty = isResultsEmpty
                                )

                                val mappedList =
                                    if (isResultsEmpty) emptyList()
                                    else response.cast.map { it.toCastItem() }
                                castAdapter.submitList(mappedList)

                                showTrailerVideoObserver(id)
                            },
                            onError = {
                                showShimmer(isLoading = false, type = DetailTypeEnum.CAST)
                                updateCastSectionState(isLoading = false, isShowEmpty = false)
                                showSnackBar(
                                    message = it,
                                    actionText = getString(R.string.action_retry)
                                ) {
                                    setCastObserver(id)
                                }
                            }
                        )
                    }

                    FROM.TV_SHOW -> {
                        detailTvShowCastResponse(id).observeNetworkResult(
                            lifecycleOwner = viewLifecycleOwner,
                            onLoading = {
                                showShimmer(isLoading = true, type = DetailTypeEnum.CAST)
                                updateCastSectionState(isLoading = true)
                            },
                            onSuccess = { response ->
                                val results = response.cast
                                val isResultsEmpty = results.isEmpty()

                                showShimmer(isLoading = false, type = DetailTypeEnum.CAST)
                                updateCastSectionState(
                                    isLoading = false,
                                    isShowEmpty = isResultsEmpty
                                )

                                val mappedList =
                                    if (isResultsEmpty) emptyList()
                                    else response.cast.map { it.toCastItem() }
                                castAdapter.submitList(mappedList)

                                showTrailerVideoObserver(id)
                            },
                            onError = {
                                showShimmer(isLoading = false, type = DetailTypeEnum.CAST)
                                updateCastSectionState(isLoading = false, isShowEmpty = false)
                                showSnackBar(
                                    message = it,
                                    actionText = getString(R.string.action_retry)
                                ) {
                                    setCastObserver(id)
                                }
                            }
                        )
                    }
                }
            }
        }
    }

    private fun setTrailerVideoAdapter() {
        binding.rvTrailerVideo.apply {
            layoutManager = LinearLayoutManager(
                requireContext(),
                LinearLayoutManager.HORIZONTAL,
                false
            )
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
                        detailMoviesVideoResponse(id).observeNetworkResult(
                            lifecycleOwner = viewLifecycleOwner,
                            onLoading = {
                                showShimmer(isLoading = true, type = DetailTypeEnum.TRAILER_VIDEO)
                                updateTrailerVideoSectionState(isLoading = true)
                            },
                            onSuccess = { response ->
                                val results = response.results
                                val isEmptyResult = results.isEmpty()

                                showShimmer(isLoading = false, type = DetailTypeEnum.TRAILER_VIDEO)
                                updateTrailerVideoSectionState(
                                    isLoading = false,
                                    isShowEmpty = isEmptyResult
                                )

                                val mappedList =
                                    if (isEmptyResult) emptyList()
                                    else results.toMovieBaseVideoItemList()
                                trailerVideoAdapter.submitList(mappedList)

                                showReviewsObserver(id)
                            },
                            onError = {
                                showShimmer(isLoading = false, type = DetailTypeEnum.TRAILER_VIDEO)
                                updateTrailerVideoSectionState(
                                    isLoading = false,
                                    isShowEmpty = false
                                )
                                showSnackBar(
                                    message = it,
                                    actionText = getString(R.string.action_retry)
                                ) {
                                    // TODO: Reload data
                                }
                            }
                        )
                    }

                    FROM.TV_SHOW -> {
                        detailTvShowVideoResponse(id).observeNetworkResult(
                            lifecycleOwner = viewLifecycleOwner,
                            onLoading = {
                                showShimmer(isLoading = true, type = DetailTypeEnum.TRAILER_VIDEO)
                                updateTrailerVideoSectionState(isLoading = true)
                            },
                            onSuccess = { response ->
                                val results = response.results
                                val isEmptyResult = results.isEmpty()

                                showShimmer(isLoading = false, type = DetailTypeEnum.TRAILER_VIDEO)
                                updateTrailerVideoSectionState(
                                    isLoading = false,
                                    isShowEmpty = isEmptyResult
                                )

                                val mappedList =
                                    if (isEmptyResult) emptyList()
                                    else response.results.toTvShowBaseVideoItemList()
                                trailerVideoAdapter.submitList(mappedList)

                                showReviewsObserver(id)
                            },
                            onError = {
                                showShimmer(isLoading = false, type = DetailTypeEnum.TRAILER_VIDEO)
                                updateTrailerVideoSectionState(
                                    isLoading = false,
                                    isShowEmpty = false
                                )
                                showSnackBar(
                                    message = it,
                                    actionText = getString(R.string.action_retry)
                                ) {
                                    // TODO: Reload data
                                }
                            }
                        )
                    }
                }
            }
        }
    }

    private fun setReviewsAdapter() {
        binding.rvReviews.apply {
            layoutManager = LinearLayoutManager(
                requireContext(),
                LinearLayoutManager.HORIZONTAL,
                false
            )
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
                        detailMovieReviewsResponse(id).observeNetworkResult(
                            lifecycleOwner = viewLifecycleOwner,
                            onLoading = {
                                showShimmer(isLoading = true, type = DetailTypeEnum.REVIEWS)
                                updateReviewsSectionState(isLoading = true)
                            },
                            onSuccess = { response ->
                                val results = response.results
                                val isResultsEmpty = results.isEmpty()

                                showShimmer(isLoading = false, type = DetailTypeEnum.REVIEWS)
                                updateReviewsSectionState(
                                    isLoading = false,
                                    isShowEmpty = isResultsEmpty
                                )

                                val mappedList =
                                    if (isResultsEmpty) emptyList() else results.map { it.toBaseReviewItem() }
                                reviewsAdapter.submitList(mappedList)

                                showSimilarMovieOrTvShowObserver(id)
                            },
                            onError = {
                                showShimmer(isLoading = false, type = DetailTypeEnum.REVIEWS)
                                updateReviewsSectionState(isLoading = false, isShowEmpty = false)
                                showSnackBar(
                                    message = it,
                                    actionText = getString(R.string.action_retry)
                                ) {
                                    // TODO: Reload data
                                }
                            }
                        )
                    }

                    FROM.TV_SHOW -> {
                        detailTvShowReviewsResponse(id).observeNetworkResult(
                            lifecycleOwner = viewLifecycleOwner,
                            onLoading = {
                                showShimmer(isLoading = true, type = DetailTypeEnum.REVIEWS)
                                updateReviewsSectionState(isLoading = true)
                            },
                            onSuccess = { response ->
                                val results = response.results
                                val isResultsEmpty = results.isEmpty()

                                showShimmer(isLoading = false, type = DetailTypeEnum.REVIEWS)
                                updateReviewsSectionState(
                                    isLoading = false,
                                    isShowEmpty = isResultsEmpty
                                )

                                val mappedList =
                                    if (isResultsEmpty) emptyList() else results.map { it.toBaseReviewItem() }
                                reviewsAdapter.submitList(mappedList)

                                showSimilarMovieOrTvShowObserver(id)
                            },
                            onError = {
                                showShimmer(isLoading = false, type = DetailTypeEnum.REVIEWS)
                                updateReviewsSectionState(isLoading = false, isShowEmpty = false)
                                showSnackBar(
                                    message = it,
                                    actionText = getString(R.string.action_retry)
                                ) {
                                    // TODO: Reload data
                                }
                            }
                        )
                    }
                }
            }
        }
    }

    private fun setSimilarMovieAdapter() {
        binding.rvMovieSimilar.apply {
            layoutManager = LinearLayoutManager(
                requireContext(),
                LinearLayoutManager.HORIZONTAL,
                false
            )
            clipToPadding = false
            clipChildren = false
            setHasFixedSize(false)
            adapter = similarAdapter

            val snapHelper = DynamicSnapHelper()
            snapHelper.attachToRecyclerView(this)
        }
    }

    private fun showSimilarMovieOrTvShowObserver(id: Int) {
        binding.apply {
            detailMovieTvShowViewModel.apply {
                when (from) {
                    FROM.MOVIE -> {
                        detailMovieSimilarResponse(id).observeNetworkResult(
                            lifecycleOwner = viewLifecycleOwner,
                            onLoading = {
                                showShimmer(isLoading = true, type = DetailTypeEnum.SIMILAR)
                                updateSimilarSectionState(isLoading = true)
                            },
                            onSuccess = { response ->
                                val results = response.results
                                val isResultsEmpty = results.isEmpty()

                                showShimmer(isLoading = false, type = DetailTypeEnum.SIMILAR)
                                updateSimilarSectionState(
                                    isLoading = false,
                                    isShowEmpty = isResultsEmpty
                                )

                                val mappedList =
                                    if (isResultsEmpty) emptyList()
                                    else results.map { it.toMovieBaseSimilarItem() }
                                similarAdapter.submitList(mappedList)
                            },
                            onError = {
                                showShimmer(isLoading = false, type = DetailTypeEnum.SIMILAR)
                                updateSimilarSectionState(isLoading = false, isShowEmpty = false)
                                showSnackBar(
                                    message = it,
                                    actionText = getString(R.string.action_retry)
                                ) {
                                    // TODO: Reload data
                                }
                            }
                        )
                    }

                    FROM.TV_SHOW -> {
                        detailTvShowSimilarResponse(id).observeNetworkResult(
                            lifecycleOwner = viewLifecycleOwner,
                            onLoading = {
                                showShimmer(isLoading = true, type = DetailTypeEnum.SIMILAR)
                                updateSimilarSectionState(isLoading = true)
                            },
                            onSuccess = { response ->
                                val results = response.results
                                val isResultsEmpty = results.isEmpty()

                                showShimmer(isLoading = false, type = DetailTypeEnum.SIMILAR)
                                updateSimilarSectionState(
                                    isLoading = false,
                                    isShowEmpty = isResultsEmpty
                                )

                                val mappedList =
                                    if (isResultsEmpty) emptyList()
                                    else results.map { it.toTvShowSimilarItem() }
                                similarAdapter.submitList(mappedList)
                            },
                            onError = {
                                showShimmer(isLoading = false, type = DetailTypeEnum.SIMILAR)
                                updateSimilarSectionState(isLoading = false, isShowEmpty = false)
                                showSnackBar(
                                    message = it,
                                    actionText = getString(R.string.action_retry)
                                ) {
                                    // TODO: Reload data
                                }
                            }
                        )
                    }
                }
            }
        }
    }

    private fun Int.convertRuntime(): String {
        val hours = this / TIME_60
        val minutes = this % TIME_60
        return getString(R.string.tvRuntimeInTime, hours, minutes)
    }

    private fun showShimmer(
        isLoading: Boolean,
        type: DetailTypeEnum,
        useInvisible: Boolean = true
    ) {
        binding.apply {
            val shimmerView = when (type) {
                DetailTypeEnum.OVERVIEW -> shimmerDetail
                DetailTypeEnum.CAST -> shimmerCast
                DetailTypeEnum.TRAILER_VIDEO -> shimmerTrailerVideo
                DetailTypeEnum.REVIEWS -> shimmerReviews
                DetailTypeEnum.SIMILAR -> shimmerSimilar
            }

            if (isLoading) {
                shimmerView.startShimmer()
                shimmerView.showView()
            } else {
                shimmerView.stopShimmer()
                shimmerView.hideView()
            }

            shimmerView.toggleVisibilityIf(isLoading, useInvisible)
        }
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
                return
            }

            groupCast.showView()
            tvCastTitle.showView()
            tvNoCast.toggleVisibilityIf(isShowEmpty)
            rvCast.toggleVisibilityIf(!isShowEmpty)
        }
    }

    private fun updateTrailerVideoSectionState(
        isLoading: Boolean = false,
        isShowEmpty: Boolean = false
    ) {
        binding.apply {
            if (isLoading) {
                groupTrailerVideo.hideView()
                return
            }

            groupTrailerVideo.showView()
            tvTrailerTitle.showView()
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
                return
            }

            groupReviews.showView()
            tvReviewsTitle.showView()
            tvNoReviews.toggleVisibilityIf(isShowEmpty)
            rvReviews.toggleVisibilityIf(!isShowEmpty)
        }
    }

    private fun updateSimilarSectionState(
        isLoading: Boolean = false,
        isShowEmpty: Boolean = false
    ) {
        binding.apply {
            if (isLoading) {
                groupSimilar.hideView()
                return
            }

            groupSimilar.showView()

            tvMovieSimilarTitle.toggleVisibilityIf(from == FROM.MOVIE)
            tvTvShowSimilarTitle.toggleVisibilityIf(from == FROM.TV_SHOW)

            tvNoSimilar.toggleVisibilityIf(isShowEmpty)
            rvMovieSimilar.toggleVisibilityIf(!isShowEmpty)
        }
    }

    companion object {
        private const val TEXT_SIZE = 12f
        const val EXTRA_ID = "EXTRA_ID"
        const val EXTRA_TITLE = "EXTRA_TITLE"
        const val EXTRA_FROM = "EXTRA_FROM"
        const val TIME_60 = 60
    }
}