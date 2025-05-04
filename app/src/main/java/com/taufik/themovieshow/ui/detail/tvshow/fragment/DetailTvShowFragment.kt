package com.taufik.themovieshow.ui.detail.tvshow.fragment

import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.SnapHelper
import com.taufik.themovieshow.R
import com.taufik.themovieshow.data.NetworkResult
import com.taufik.themovieshow.databinding.FragmentDetailTvShowBinding
import com.taufik.themovieshow.model.response.tvshow.detail.TvShowsPopularDetailResponse
import com.taufik.themovieshow.ui.detail.tvshow.adapter.TvShowSimilarAdapter
import com.taufik.themovieshow.ui.detail.tvshow.adapter.TvShowTrailerVideoAdapter
import com.taufik.themovieshow.ui.detail.tvshow.adapter.TvShowsCastAdapter
import com.taufik.themovieshow.ui.movie.adapter.ReviewsAdapter
import com.taufik.themovieshow.ui.tvshow.viewmodel.DetailTvShowViewModel
import com.taufik.themovieshow.utils.CommonDateFormatConstants
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
import com.taufik.themovieshow.utils.extensions.toRating
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class DetailTvShowFragment : Fragment() {

    private var _binding: FragmentDetailTvShowBinding? = null
    private val binding get() = _binding!!

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

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailTvShowBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
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
            data?.let { detailResponse ->
                imgPoster.loadImage(detailResponse.posterPath.orEmpty())
                imgBackdrop.loadImage(detailResponse.backdropPath)
                tvTitle.text = detailResponse.name

                val startedOn = detailResponse.firstAirDate.convertDate(
                    CommonDateFormatConstants.YYYY_MM_DD_FORMAT,
                    CommonDateFormatConstants.EEE_D_MMM_YYYY_FORMAT
                )
                tvStartedOn.stringFormat(getString(R.string.tvStartedOn), startedOn)
                tvStatus.text = detailResponse.status

                val networkFirst = detailResponse.networks.first()
                if (detailResponse.networks.isEmpty())
                    tvNetwork.text = getString(R.string.tvNA)
                else if (detailResponse.networks.first().originCountry.isEmpty())
                    tvNetwork.stringFormat(networkFirst.name, "(${getString(R.string.tvNA)})")
                else
                    tvNetwork.stringFormat(networkFirst.name, "(${networkFirst.originCountry})")

                // Handle Overview
                if (detailResponse.overview.isEmpty()) {
                    tvOverview.hideView()
                    tvNoOverview.showView()
                    tvReadMore.hideView()
                } else {
                    tvNoOverview.hideView()
                    tvOverview.apply {
                        showView()
                        text = detailResponse.overview
                    }
                    tvReadMore.showView()
                }

                // Handle Rating
                tvRating.text = if (detailResponse.voteAverage.toString().isEmpty())
                    getString(R.string.tvNA)
                else
                    detailResponse.voteAverage.toRating()

                // Handle Language
                tvLanguage.text = when {
                    detailResponse.spokenLanguages.isNotEmpty() -> detailResponse.spokenLanguages.first().englishName
                    detailResponse.originalLanguage.isNotEmpty() -> detailResponse.originalLanguage
                    else -> getString(R.string.tvNA)
                }

                // Handle Country
                tvCountry.text = if (detailResponse.originCountry.isNotEmpty())
                    detailResponse.originCountry.joinToString { it }
                else
                    getString(R.string.tvNA)

                // Handle Episodes
                if (detailResponse.numberOfEpisodes.toString().isEmpty())
                    tvEpisodes.text = getString(R.string.tvNA)
                else tvEpisodes.stringFormat(
                    detailResponse.numberOfEpisodes.toString(),
                    getString(R.string.tvEps)
                )

                // Handle Genres
                if (detailResponse.genres.isEmpty()) {
                    tvNoGenres.showView()
                    tvGenre.hideView()
                } else {
                    tvNoGenres.hideView()
                    tvGenre.apply {
                        showView()
                        text = detailResponse.genres.joinToString { it.name }
                    }
                }

                checkFavoriteData(idTvShow)
                setActionFavorite(
                    idTvShow,
                    detailResponse.posterPath.orEmpty(),
                    title,
                    detailResponse.firstAirDate,
                    detailResponse.voteAverage
                )
                shareTvShow(detailResponse.homepage)
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
        _binding = null
        trailerVideoAdapter = null
        similarAdapter = null
    }

    companion object {
        const val EXTRA_ID = "EXTRA_ID"
        const val EXTRA_TITLE = "EXTRA_TITLE"
    }
}