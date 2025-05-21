package com.taufik.themovieshow.ui.detail.tvshow.fragment

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
import com.taufik.themovieshow.base.fragment.BaseFragment
import com.taufik.themovieshow.data.NetworkResult
import com.taufik.themovieshow.databinding.FragmentDetailTvShowBinding
import com.taufik.themovieshow.model.response.tvshow.detail.TvShowsPopularDetailResponse
import com.taufik.themovieshow.ui.detail.tvshow.adapter.TvShowSimilarAdapter
import com.taufik.themovieshow.ui.detail.tvshow.adapter.TvShowTrailerVideoAdapter
import com.taufik.themovieshow.ui.detail.tvshow.adapter.TvShowsCastAdapter
import com.taufik.themovieshow.ui.movie.adapter.ReviewsAdapter
import com.taufik.themovieshow.ui.tvshow.viewmodel.DetailTvShowViewModel
import com.taufik.themovieshow.utils.objects.CommonDateFormatConstants
import com.taufik.themovieshow.utils.extensions.applySystemBarBottomPadding
import com.taufik.themovieshow.utils.extensions.convertDate
import com.taufik.themovieshow.utils.extensions.hideView
import com.taufik.themovieshow.utils.extensions.loadImage
import com.taufik.themovieshow.utils.extensions.navigateToDetailTvShow
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
class DetailTvShowFragment : BaseFragment<FragmentDetailTvShowBinding>() {

    private val viewModel: DetailTvShowViewModel by viewModels()
    private val castAdapter by lazy { TvShowsCastAdapter() }
    private var trailerVideoAdapter: TvShowTrailerVideoAdapter? = null
    private val reviewsAdapter by lazy { ReviewsAdapter() }
    private var similarAdapter: TvShowSimilarAdapter? = null

    private var idTvShow = 0
    private var title = ""
    private var isChecked = false

    private val backPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            this@DetailTvShowFragment.popBackStack()
        }
    }

    override fun inflateBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentDetailTvShowBinding = FragmentDetailTvShowBinding.inflate(inflater, container, false)

    override fun onFragmentReady(savedInstanceState: Bundle?) {
        applySystemBarBottomPadding()

        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            backPressedCallback
        )
        getBundleData()
        showToolbarData()
        setDetailObserver(idTvShow)
        setCastAdapter()
        setCastObserver(idTvShow)
        setTrailerVideoAdapter()
        showTrailerVideoObserver(idTvShow)
        setReviewsAdapter()
        showReviewsObserver(idTvShow)
        setSimilarAdapter()
        showSimilarObserver(idTvShow)
        setReadMore()
    }

    private fun getBundleData() {
        idTvShow = arguments?.getInt(EXTRA_ID, 0) ?: 0
        title = arguments?.getString(EXTRA_TITLE).orEmpty()
    }

    private fun showToolbarData() {
        binding.toolbarDetailTvShow.apply {
            tvToolbar.text = title
            imgBack.setOnClickListener {
                this@DetailTvShowFragment.popBackStack()
            }
        }
    }

    private fun setDetailObserver(id: Int) {
        viewModel.apply {
            setDetailTvShowPopular(id)
            detailTvShowPopularResponse.observe(viewLifecycleOwner) { response ->
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

    private fun showDetailData(data: TvShowsPopularDetailResponse?) {
        binding.apply {
            data?.let { tvShow ->

                // Poster
                imgPoster.loadImage(tvShow.posterPath.orEmpty())

                // Backdrop
                imgBackdrop.loadImage(tvShow.backdropPath)

                // Title
                tvTitle.text = tvShow.name.ifEmpty { getString(R.string.tvNA) }

                // Network
                val networkText = when {
                    tvShow.networks.isEmpty() -> getString(R.string.tvNA)
                    tvShow.networks.first().originCountry.isEmpty() -> getString(R.string.tvNetworkDesc, tvShow.networks.first().name, getString(R.string.tvNA))
                    else -> getString(R.string.tvNetworkDesc, tvShow.networks.first().name, tvShow.networks.first().originCountry)
                }
                tvNetwork.text = networkText

                val formattedDate = if (tvShow.firstAirDate.isEmpty()) {
                    getString(R.string.tvNA)
                } else {
                    tvShow.firstAirDate.convertDate(
                        CommonDateFormatConstants.YYYY_MM_DD_FORMAT,
                        CommonDateFormatConstants.EEE_D_MMM_YYYY_FORMAT
                    )
                }

                // Release date
                tvStartedOn.apply {
                    stringFormat(getString(R.string.tvStartedOn), formattedDate)
                    toggleVisibilityIf(true)
                }

                // Release status
                val releaseStatus = if (tvShow.status.isEmpty()) {
                    getString(R.string.tvNA)
                } else {
                    tvShow.status
                }
                tvReleaseStatus.apply {
                    stringReleaseFormat(getString(R.string.tvStatus), releaseStatus)
                    toggleVisibilityIf(true)
                }

                // Rating
                val hasRating = tvShow.voteAverage != 0.0
                icTxtRating.apply {
                    setIcon(R.drawable.ic_outline_rate)
                    requireContext().setIconColor(R.color.colorTextOther)
                    setText(
                        if (hasRating) {
                            getString(R.string.tvRatingDesc, tvShow.voteAverage.toRating(), tvShow.voteCount.toString())
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

                // Episode
                icTxtEpisodes.apply {
                    setIcon(R.drawable.ic_outline_episode)
                    requireContext().setIconColor(R.color.colorTextOther)
                    setText(
                        if (tvShow.numberOfEpisodes == 0) getString(R.string.tvNA)
                        else getString(R.string.tvEpsDesc, tvShow.numberOfEpisodes.toString(), getString(R.string.tvEps))
                    )
                    setTextSize(TEXT_SIZE)
                    requireContext().setTextColor(R.color.colorTextOther)
                }

                // Age rating
                icTxtAgeRating.apply {
                    setIcon(if (tvShow.adult) R.drawable.ic_outline_adult else R.drawable.ic_outline_no_adult)
                    requireContext().setIconColor(R.color.colorTextOther)
                    setText(if (tvShow.adult) getString(R.string.tvAdults) else getString(R.string.tvAllAges))
                    setTextSize(TEXT_SIZE)
                    requireContext().setTextColor(R.color.colorTextOther)
                }

                // Language
                icTxtLanguage.apply {
                    setIcon(R.drawable.ic_outline_spoken_language)
                    requireContext().setIconColor(R.color.colorTextOther)
                    setText(
                        if (tvShow.spokenLanguages.isEmpty()) getString(R.string.tvNA)
                        else tvShow.spokenLanguages.joinToString(", ") { it.englishName }
                    )
                    setTextSize(TEXT_SIZE)
                    requireContext().setTextColor(R.color.colorTextOther)
                }

                // Genres
                icTxtGenre.apply {
                    setIcon(R.drawable.ic_outline_genre)
                    requireContext().setIconColor(R.color.colorTextOther)
                    setTextSize(TEXT_SIZE)
                    setText(
                        if (tvShow.genres.isNotEmpty()) tvShow.genres.joinToString { it.name }
                        else getString(R.string.tvNA)
                    )
                    requireContext().setTextColor(R.color.colorTextOther)
                }

                // Overview
                val hasOverview = tvShow.overview.isNotEmpty()
                tvOverview.apply {
                    text = tvShow.overview
                    toggleVisibilityIf(hasOverview)
                }
                tvNoOverview.toggleVisibilityIf(!hasOverview)
                tvReadMore.toggleVisibilityIf(hasOverview)

                // Action
                checkFavoriteData(idTvShow)
                setActionFavorite(
                    idTvShow,
                    tvShow.posterPath.orEmpty(),
                    tvShow.name,
                    tvShow.firstAirDate,
                    tvShow.voteAverage
                )
                shareTvShow(tvShow.homepage)
            }
        }
    }

    private fun checkFavoriteData(id: Int) {
        binding.apply {
            lifecycleScope.launch {
                val count = viewModel.checkFavoriteTvShow(id)
                toolbarDetailTvShow.toggleFavorite.isChecked = count > 0
                isChecked = count > 0
            }
        }
    }

    private fun setActionFavorite(
        id: Int,
        posterPath: String,
        title: String,
        firstAirDate: String,
        voteAverage: Double
    ) {
        binding.toolbarDetailTvShow.toggleFavorite.setOnClickListener {
            isChecked = !isChecked
            if (isChecked) {
                viewModel.addTvShowFavorite(
                    id,
                    posterPath,
                    title,
                    firstAirDate,
                    voteAverage
                )
                requireContext().showSuccessToastyIcon(getString(R.string.action_added_to_favorite))
            } else {
                viewModel.removeTvShowFromFavorite(id)
                requireContext().showSuccessToastyIcon(getString(R.string.action_removed_from_favorite))
            }
        }
    }

    private fun shareTvShow(link: String) {
        binding.toolbarDetailTvShow.imgShare.setOnClickListener {
            requireContext().share(getString(R.string.tvVisitTvShow), link)
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
        binding.rvTvShowCast.apply {
            val helper: SnapHelper = LinearSnapHelper()
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            helper.attachToRecyclerView(this)
            setHasFixedSize(true)
            adapter = castAdapter
        }
    }

    private fun setCastObserver(id: Int) {
        binding.apply {
            viewModel.apply {
                setDetailTvShowsCast(id)
                detailTvShowCastResponse.observe(viewLifecycleOwner) { response ->
                    when (response) {
                        is NetworkResult.Loading -> tvNoCast.hideView()
                        is NetworkResult.Success -> {
                            val data = response.data
                            if (data != null && data.cast.isNotEmpty()) {
                                castAdapter.submitList(data.cast)
                                tvNoCast.hideView()
                            } else {
                                tvNoCast.showView()
                            }
                        }

                        is NetworkResult.Error -> {
                            tvNoCast.hideView()
                        }
                    }
                }
            }
        }
    }

    private fun setTrailerVideoAdapter() {
        trailerVideoAdapter = TvShowTrailerVideoAdapter {
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
        viewModel.apply {
            setDetailTvShowVideo(id)
            detailTvShowVideoResponse.observe(viewLifecycleOwner) { response ->
                when (response) {
                    is NetworkResult.Loading -> showVideo(false)
                    is NetworkResult.Success -> {
                        val data = response.data
                        data?.let { tvShowVideoResponse ->
                            when {
                                tvShowVideoResponse.results.isEmpty() -> showVideo(false)
                                else -> {
                                    showVideo(true)
                                    trailerVideoAdapter?.submitList(tvShowVideoResponse.results)
                                }
                            }
                        }
                    }
                    is NetworkResult.Error -> showVideo(false)
                }
            }
        }
    }

    private fun setReviewsAdapter() {
        binding.rvTvShowReviews.apply {
            val helper: SnapHelper = LinearSnapHelper()
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            helper.attachToRecyclerView(this)
            setHasFixedSize(true)
            adapter = reviewsAdapter
        }
    }

    private fun showReviewsObserver(id: Int) {
        binding.apply {
            viewModel.apply {
                setDetailTvShowsReviews(id)
                detailTvShowReviewsResponse.observe(viewLifecycleOwner) { response ->
                    when (response) {
                        is NetworkResult.Loading -> tvNoReviews.isVisible = false
                        is NetworkResult.Success -> {
                            val data = response.data
                            if (data != null && data.results.isNotEmpty()) {
                                reviewsAdapter.submitList(data.results)
                                tvNoReviews.isVisible = false
                            } else {
                                tvNoReviews.isVisible = true
                            }
                        }
                        is NetworkResult.Error -> tvNoReviews.isVisible = false
                    }
                }
            }
        }
    }

    private fun setSimilarAdapter() {
        similarAdapter = TvShowSimilarAdapter {
            navigateToDetailTvShow(it.id, it.name)
        }
        binding.rvTvShowSimilar.apply {
            val helper: SnapHelper = LinearSnapHelper()
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            helper.attachToRecyclerView(this)
            setHasFixedSize(true)
            adapter = similarAdapter
        }
    }

    private fun showSimilarObserver(id: Int) {
        viewModel.apply {
            setDetailTvShowsSimilar(id)
            detailTvShowSimilarResponse.observe(viewLifecycleOwner) { response ->
                when (response) {
                    is NetworkResult.Loading -> showNoSimilarTvShow(false)
                    is NetworkResult.Success -> {
                        val results = response.data?.results
                        if (results.isNullOrEmpty()) {
                            showNoSimilarTvShow(true)
                        } else {
                            showNoSimilarTvShow(false)
                            similarAdapter?.submitList(results)
                        }
                    }
                    is NetworkResult.Error -> showNoSimilarTvShow(false)
                }
            }
        }
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

    private fun showNoSimilarTvShow(isShow: Boolean) {
        binding.apply {
            rvTvShowSimilar.isVisible = !isShow
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
    }
}