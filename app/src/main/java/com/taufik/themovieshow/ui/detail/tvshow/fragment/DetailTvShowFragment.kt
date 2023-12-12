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
import com.taufik.themovieshow.utils.convertDate
import com.taufik.themovieshow.utils.loadImage
import com.taufik.themovieshow.utils.navigateToDetailTvShow
import com.taufik.themovieshow.utils.popBackStack
import com.taufik.themovieshow.utils.share
import com.taufik.themovieshow.utils.showSuccessToastyIcon
import com.taufik.themovieshow.utils.showTrailerVideo
import com.taufik.themovieshow.utils.stringFormat
import com.taufik.themovieshow.utils.toRating
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

    private val backPressedCallback: OnBackPressedCallback = object : OnBackPressedCallback(true) {
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

        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            backPressedCallback
        )
        getBundleData()
        showToolbarData()
        setDetailObserver()
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

    private fun setDetailObserver() {
        viewModel.apply {
            setDetailTvShowPopular(idTvShow)
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

                when {
                    detailResponse.networks.isEmpty() -> tvNetwork.text = getString(R.string.tvNA)
                    detailResponse.networks.first().originCountry.isEmpty() -> tvNetwork.text = String.format("${detailResponse.networks.first().name} ${getString(R.string.tvNA)})")
                    detailResponse.overview.isEmpty() -> {
                        tvOverview.isVisible = false
                        tvNoOverview.isVisible = true
                        tvReadMore.isVisible = false
                    }
                    detailResponse.voteAverage.toString().isEmpty() -> tvRating.text = getString(R.string.tvNA)
                    detailResponse.originalLanguage.isEmpty() -> tvLanguage.text = getString(R.string.tvNA)
                    detailResponse.originCountry.isEmpty() -> tvCountry.text = getString(R.string.tvNA)
                    detailResponse.numberOfEpisodes.toString().isEmpty() -> tvEpisodes.text = getString(R.string.tvNA)
                    detailResponse.genres.isEmpty() -> showNoGenres(true)

                    else -> {
                        tvNetwork.stringFormat(detailResponse.networks.first().name, "(${detailResponse.networks.first().originCountry})")
                        tvNoOverview.isVisible = false
                        tvOverview.apply {
                            isVisible = true
                            text = detailResponse.overview
                        }

                        tvRating.text = toRating(detailResponse.voteAverage)
                        tvLanguage.text =
                            if (detailResponse.spokenLanguages.isNotEmpty())
                                detailResponse.spokenLanguages.first().englishName
                            else
                                detailResponse.originalLanguage

                        tvCountry.text = detailResponse.originCountry.joinToString { countries -> countries }
                        tvEpisodes.stringFormat(detailResponse.numberOfEpisodes.toString(), getString(R.string.tvEps))

                        showNoGenres(false)
                        tvGenre.text = detailResponse.genres.joinToString { genres -> genres.name }
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
                if (count > 0) {
                    toolbarDetailTvShow.toggleFavorite.isChecked = true
                    isChecked = true
                } else {
                    toolbarDetailTvShow.toggleFavorite.isChecked = false
                    isChecked = false
                }
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
        binding.apply {
            toolbarDetailTvShow.toggleFavorite.setOnClickListener {
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
        viewModel.apply {
            setDetailTvShowsCast(id)
            detailTvShowCastResponse.observe(viewLifecycleOwner) { response ->
                when (response) {
                    is NetworkResult.Loading -> {
                        showNoCast(false)
                    }
                    is NetworkResult.Success -> {
                        val data = response.data
                        if (data != null && data.cast.isNotEmpty()) {
                            castAdapter.submitList(data.cast)
                            showNoCast(false)
                        } else {
                            showNoCast(true)
                        }
                    }
                    is NetworkResult.Error -> {
                        showNoCast(false)
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

    private fun showNoGenres(isShow: Boolean) = binding.tvNoGenres.isVisible == isShow

    private fun showNoCast(isShow: Boolean) = binding.tvNoCast.isVisible == isShow

    private fun showNoSimilarTvShow(isShow: Boolean) {
        binding.apply {
            if (isShow) {
                rvTvShowSimilar.isVisible = false
                tvNoSimilar.isVisible = true
            } else {
                rvTvShowSimilar.isVisible = true
                tvNoSimilar.isVisible = false
            }
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