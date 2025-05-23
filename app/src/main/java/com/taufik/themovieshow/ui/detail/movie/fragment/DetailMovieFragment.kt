package com.taufik.themovieshow.ui.detail.movie.fragment

import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.SnapHelper
import com.taufik.themovieshow.R
import com.taufik.themovieshow.base.BaseFragment
import com.taufik.themovieshow.data.NetworkResult
import com.taufik.themovieshow.databinding.FragmentDetailMovieBinding
import com.taufik.themovieshow.model.response.movie.detail.MovieDetailResponse
import com.taufik.themovieshow.ui.detail.movie.adapter.MovieCastAdapter
import com.taufik.themovieshow.ui.detail.movie.adapter.MovieSimilarAdapter
import com.taufik.themovieshow.ui.detail.movie.adapter.MovieTrailerVideoAdapter
import com.taufik.themovieshow.ui.movie.adapter.ReviewsAdapter
import com.taufik.themovieshow.ui.movie.viewmodel.DetailMovieViewModel
import com.taufik.themovieshow.utils.CommonDateFormatConstants
import com.taufik.themovieshow.utils.extensions.applySystemBarBottomPadding
import com.taufik.themovieshow.utils.extensions.convertDate
import com.taufik.themovieshow.utils.extensions.hideView
import com.taufik.themovieshow.utils.extensions.loadImage
import com.taufik.themovieshow.utils.extensions.navigateToDetailMovie
import com.taufik.themovieshow.utils.extensions.popBackStack
import com.taufik.themovieshow.utils.extensions.share
import com.taufik.themovieshow.utils.extensions.showSuccessToastyIcon
import com.taufik.themovieshow.utils.extensions.showTrailerVideo
import com.taufik.themovieshow.utils.extensions.showView
import com.taufik.themovieshow.utils.extensions.stringFormat
import com.taufik.themovieshow.utils.extensions.stringReleaseFormat
import com.taufik.themovieshow.utils.extensions.toRating
import com.taufik.themovieshow.utils.extensions.toggleVisibilityIf
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class DetailMovieFragment : BaseFragment<FragmentDetailMovieBinding>() {

    private val viewModel: DetailMovieViewModel by viewModels()
    private val castAdapter by lazy { MovieCastAdapter() }
    private var trailerVideoAdapter: MovieTrailerVideoAdapter? = null
    private val reviewsAdapter by lazy { ReviewsAdapter() }
    private var similarAdapter: MovieSimilarAdapter? = null

    private var idMovie = 0
    private var title = ""
    private var isChecked = false

    private val backPressedCallback: OnBackPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            this@DetailMovieFragment.popBackStack()
        }
    }

    override fun inflateBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentDetailMovieBinding = FragmentDetailMovieBinding.inflate(inflater, container, false)

    override fun onFragmentReady(savedInstanceState: Bundle?) {
        applySystemBarBottomPadding()

        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            backPressedCallback
        )
        getBundleData()
        showToolbarData()
        setDetailObserver(idMovie)
        setCastAdapter()
        setCastObserver(idMovie)
        setTrailerVideoAdapter()
        showTrailerVideoObserver(idMovie)
        setReviewsAdapter()
        showReviewsObserver(idMovie)
        setSimilarMovieAdapter()
        showSimilarObserver(idMovie)
        setReadMore()
    }

    private fun getBundleData() {
        idMovie = arguments?.getInt(EXTRA_ID, 0) ?: 0
        title = arguments?.getString(EXTRA_TITLE).orEmpty()
    }

    private fun showToolbarData() {
        binding.apply {
            toolbarDetailMovie.apply {
                tvToolbar.text = title
                imgBack.setOnClickListener {
                    this@DetailMovieFragment.popBackStack()
                }
            }
        }
    }

    private fun setDetailObserver(id: Int) {
        viewModel.apply {
            setDetailMovies(id)
            detailMoviesResponse.observe(viewLifecycleOwner) { response ->
                when (response) {
                    is NetworkResult.Loading -> {
                        // loading component not available
                    }
                    is NetworkResult.Success -> showDetailData(response.data)
                    is NetworkResult.Error -> {
                        // error message not available
                    }
                }
            }
        }
    }

    private fun showDetailData(data: MovieDetailResponse?) {
        binding.apply {
            data?.let { movie ->

                // Poster
                imgPoster.loadImage(movie.posterPath.orEmpty())

                // Backdrop
                imgBackdrop.loadImage(movie.backdropPath)

                // Title
                tvTitle.text = movie.title.ifEmpty { getString(R.string.tvNA) }

                // Release date
                val formattedDate = if (movie.releaseDate.isEmpty()) {
                    getString(R.string.tvNA)
                } else {
                    movie.releaseDate.convertDate(
                        CommonDateFormatConstants.YYYY_MM_DD_FORMAT,
                        CommonDateFormatConstants.EEE_D_MMM_YYYY_FORMAT
                    )
                }
                tvReleasedOn.apply {
                    stringFormat(getString(R.string.tvReleasedOn), formattedDate)
                    toggleVisibilityIf(true)
                }

                // Release status
                val releaseStatus = if (movie.status.isEmpty()) {
                    getString(R.string.tvNA)
                } else {
                    movie.status
                }
                tvReleaseStatus.apply {
                    stringReleaseFormat(getString(R.string.tvStatus), releaseStatus)
                    toggleVisibilityIf(true)
                }

                // Rating
                val hasRating = movie.voteAverage != 0.0
                icTxtRating.apply {
                    setIcon(R.drawable.ic_outline_rate)
                    requireContext().setIconColor(R.color.colorTextOther)
                    setText(
                        if (hasRating) {
                            getString(
                                R.string.tvRatingDesc,
                                movie.voteAverage.toRating(),
                                movie.voteCount.toString()
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

                // Runtime
                val timeInZero = 0
                icTxtRuntime.apply {
                    setIcon(R.drawable.ic_outline_runtime)
                    requireContext().setIconColor(R.color.colorTextOther)
                    setText(
                        if (movie.runtime == 0) convertRuntime(timeInZero)
                        else convertRuntime(movie.runtime)
                    )
                    setTextSize(TEXT_SIZE)
                    requireContext().setTextColor(R.color.colorTextOther)
                }

                // Age rating
                icTxtAgeRating.apply {
                    setIcon(if (movie.adult) R.drawable.ic_outline_adult else R.drawable.ic_outline_no_adult)
                    requireContext().setIconColor(R.color.colorTextOther)
                    setText(if (movie.adult) getString(R.string.tvAdults) else getString(R.string.tvAllAges))
                    setTextSize(TEXT_SIZE)
                    requireContext().setTextColor(R.color.colorTextOther)
                }

                // Language
                icTxtLanguage.apply {
                    setIcon(R.drawable.ic_outline_spoken_language)
                    requireContext().setIconColor(R.color.colorTextOther)
                    setText(
                        if (movie.spokenLanguages.isEmpty()) {
                        getString(R.string.tvNA)
                    } else {
                        movie.spokenLanguages.joinToString(", ") { it.englishName }
                    })
                    setTextSize(TEXT_SIZE)
                    requireContext().setTextColor(R.color.colorTextOther)
                }

                // Genres
                icTxtGenre.apply {
                    setIcon(R.drawable.ic_outline_genre)
                    requireContext().setIconColor(R.color.colorTextOther)
                    setText(
                        if (movie.genres.isNotEmpty()) movie.genres.joinToString { it.name }
                        else getString(R.string.tvNA)
                    )
                    setTextSize(TEXT_SIZE)
                    requireContext().setTextColor(R.color.colorTextOther)
                }

                // Overview
                val hasOverview = movie.overview.isNotEmpty()
                tvOverview.apply {
                    text = movie.overview
                    toggleVisibilityIf(hasOverview)
                }
                tvNoOverview.toggleVisibilityIf(!hasOverview)
                tvReadMore.toggleVisibilityIf(hasOverview)

                // Action
                checkFavoriteData(idMovie)
                setActionFavorite(
                    idMovie,
                    movie.posterPath.orEmpty(),
                    movie.title,
                    movie.releaseDate,
                    movie.voteAverage
                )
                shareMovie(movie.homepage)
            }
        }
    }

    private fun checkFavoriteData(id: Int) {
        binding.apply {
            lifecycleScope.launch {
                val count = viewModel.checkFavoriteMovie(id)
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
                if (isChecked) {
                    viewModel.addMovieToFavorite(
                        id,
                        posterPath,
                        title,
                        releaseDate,
                        voteAverage
                    )
                    requireContext().showSuccessToastyIcon(getString(R.string.action_added_to_favorite))
                } else {
                    viewModel.removeMovieFromFavorite(id)
                    requireContext().showSuccessToastyIcon(getString(R.string.action_removed_from_favorite))
                }
            }
        }
    }

    private fun shareMovie(link: String) {
        binding.toolbarDetailMovie.imgShare.setOnClickListener {
            requireContext().share(getString(R.string.tvVisitMovie), link)
        }
    }

    private fun setReadMore() {
        binding.apply {
            tvReadMore.isVisible = true
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
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            helper.attachToRecyclerView(this)
            setHasFixedSize(true)
            adapter = castAdapter
        }
    }

    private fun setCastObserver(id: Int) {
        binding.apply {
            viewModel.apply {
                setDetailMovieCast(id)
                detailMoviesCastResponse.observe(viewLifecycleOwner) { response ->
                    when (response) {
                        is NetworkResult.Loading -> tvNoCast.hideView()
                        is NetworkResult.Success -> {
                            val cast = response.data?.cast
                            if (cast.isNullOrEmpty()) {
                                tvNoCast.showView()
                            } else {
                                tvNoCast.hideView()
                                castAdapter.submitList(cast)
                            }
                        }

                        is NetworkResult.Error -> tvNoCast.hideView()
                    }
                }
            }
        }
    }

    private fun setTrailerVideoAdapter() {
        trailerVideoAdapter = MovieTrailerVideoAdapter {
            requireContext().showTrailerVideo(it.key)
        }

        binding.rvTrailerVideo.apply {
            val helper: SnapHelper = LinearSnapHelper()
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            helper.attachToRecyclerView(this)
            setHasFixedSize(true)
            adapter = trailerVideoAdapter
        }
    }

    private fun showTrailerVideoObserver(id: Int) {
        viewModel.apply {
            setDetailMovieVideo(id)
            detailMoviesVideoResponse.observe(viewLifecycleOwner) { response ->
                when (response) {
                    is NetworkResult.Loading -> showVideo(false)
                    is NetworkResult.Success -> {
                        val results = response.data?.results
                        if (results.isNullOrEmpty()) {
                            showVideo(false)
                        } else {
                            showVideo(true)
                            trailerVideoAdapter?.submitList(results)
                        }
                    }

                    is NetworkResult.Error -> showVideo(false)
                }
            }
        }
    }

    private fun setReviewsAdapter() {
        binding.rvMovieReviews.apply {
            val helper: SnapHelper = LinearSnapHelper()
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            helper.attachToRecyclerView(this)
            setHasFixedSize(true)
            adapter = reviewsAdapter
        }
    }

    private fun showReviewsObserver(id: Int) {
        binding.apply {
            viewModel.apply {
                setDetailMovieReviews(id)
                detailMovieReviewsResponse.observe(viewLifecycleOwner) { response ->
                    when (response) {
                        is NetworkResult.Loading -> tvNoReviews.isVisible = false
                        is NetworkResult.Success -> {
                            val results = response.data?.results
                            if (results.isNullOrEmpty()) {
                                tvNoReviews.isVisible = true
                            } else {
                                tvNoReviews.isVisible = false
                                reviewsAdapter.submitList(results)
                            }
                        }

                        is NetworkResult.Error -> tvNoReviews.isVisible = false
                    }
                }
            }
        }
    }

    private fun setSimilarMovieAdapter() {
        similarAdapter = MovieSimilarAdapter {
            navigateToDetailMovie(it.id, it.title)
        }
        binding.rvMovieSimilar.apply {
            val helper: SnapHelper = LinearSnapHelper()
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            helper.attachToRecyclerView(this)
            setHasFixedSize(true)
            adapter = similarAdapter
        }
    }

    private fun showSimilarObserver(id: Int) {
        viewModel.apply {
            setDetailMovieSimilar(id)
            detailMovieSimilarResponse.observe(viewLifecycleOwner) { response ->
                when (response) {
                    is NetworkResult.Loading -> showNoSimilarMovie(false)
                    is NetworkResult.Success -> {
                        val results = response.data?.results
                        if (results.isNullOrEmpty()) {
                            showNoSimilarMovie(true)
                        } else {
                            showNoSimilarMovie(false)
                            similarAdapter?.submitList(results)
                        }
                    }

                    is NetworkResult.Error -> showNoSimilarMovie(false)
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
            if (isShow) {
                rvTrailerVideo.isVisible = true
                tvNoVideo.isVisible = false
            } else {
                rvTrailerVideo.isVisible = false
                tvNoVideo.isVisible = true
            }
        }
    }

    private fun showNoSimilarMovie(isShow: Boolean) {
        binding.apply {
            rvMovieSimilar.isVisible = !isShow
            tvNoSimilar.isVisible = isShow
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        trailerVideoAdapter = null
        similarAdapter = null
    }

    companion object {
        private const val TEXT_SIZE = 12f
        const val EXTRA_ID = "EXTRA_ID"
        const val EXTRA_TITLE = "EXTRA_TITLE"
        const val TIME_60 = 60
    }
}