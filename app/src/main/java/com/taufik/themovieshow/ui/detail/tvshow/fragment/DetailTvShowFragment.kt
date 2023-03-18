package com.taufik.themovieshow.ui.detail.tvshow.fragment

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.SnapHelper
import com.taufik.themovieshow.R
import com.taufik.themovieshow.ui.main.tvshow.viewmodel.DetailTvShowViewModel
import com.taufik.themovieshow.databinding.FragmentDetailTvShowBinding
import com.taufik.themovieshow.ui.detail.tvshow.adapter.TvShowSimilarAdapter
import com.taufik.themovieshow.ui.detail.tvshow.adapter.TvShowTrailerVideoAdapter
import com.taufik.themovieshow.ui.detail.tvshow.adapter.TvShowsCastAdapter
import com.taufik.themovieshow.ui.main.movie.adapter.ReviewsAdapter
import com.taufik.themovieshow.utils.CommonDateFormatConstants
import com.taufik.themovieshow.utils.convertDate
import com.taufik.themovieshow.utils.loadImage
import com.taufik.themovieshow.utils.showToasty
import com.taufik.themovieshow.utils.toRating
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DetailTvShowFragment : Fragment() {

    private var _binding: FragmentDetailTvShowBinding? = null
    private val binding get() = _binding!!

    private val viewModel: DetailTvShowViewModel by viewModels()
    private val castAdapter by lazy { TvShowsCastAdapter() }
    private val trailerVideoAdapter by lazy { TvShowTrailerVideoAdapter() }
    private val reviewsAdapter by lazy { ReviewsAdapter() }
    private val similarAdapter by lazy { TvShowSimilarAdapter() }

    private var idTvShow = 0
    private var title = ""
    private var isChecked = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentDetailTvShowBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getBundleData()
        showToolbarData()
        setData()
        setReadMore()
        setCastAdapter()
        setTrailerVideoAdapter()
        setReviewsAdapter()
        setSimilarAdapter()
    }

    private fun getBundleData() {
        idTvShow = arguments?.getInt(EXTRA_ID, 0) ?: 0
        title = arguments?.getString(EXTRA_TITLE, "") ?: ""
    }

    private fun showToolbarData() = with(binding) {
        toolbarDetailTvShow.apply {
            tvToolbar.text = title
            imgBack.setOnClickListener {
                findNavController().popBackStack()
            }
        }
    }

    private fun setData() = with(binding) {
        viewModel.apply {
            setDetailTvShowPopular(idTvShow)
            detailTvShows.observe(viewLifecycleOwner) {
                if (it != null) {
                    imgPoster.loadImage(it.posterPath)
                    imgBackdrop.loadImage(it.backdropPath)
                    tvTitle.text = it.name

                    when {
                        it.networks.isEmpty() -> tvNetwork.text = "(N/A)"
                        it.networks[0].originCountry == "" -> tvNetwork.text =
                            String.format("${it.networks[0].name} (N/A)")
                        else -> tvNetwork.text =
                            String.format("${it.networks[0].name} (${it.networks[0].originCountry})")
                    }

                    val startedOn = it.firstAirDate.convertDate(
                        CommonDateFormatConstants.YYYY_MM_DD_FORMAT,
                        CommonDateFormatConstants.EEE_D_MMM_YYYY_FORMAT
                    )
                    tvStartedOn.text =
                        String.format("%s %s", getString(R.string.tvStartedOn), startedOn)

                    tvStatus.text = it.status

                    when {
                        it.overview.isEmpty() -> {
                            tvOverview.isVisible = false
                            tvNoOverview.isVisible = true
                            tvReadMore.isVisible = false
                        }
                        else -> {
                            tvNoOverview.isVisible = false
                            tvOverview.apply {
                                isVisible = true
                                text = it.overview
                            }
                        }
                    }

                    when {
                        it.voteAverage.toString().isEmpty() -> tvRating.text =
                            getString(R.string.tvNA)
                        else -> tvRating.text = toRating(it.voteAverage)
                    }

                    when {
                        it.originalLanguage.isEmpty() -> tvLanguage.text = getString(R.string.tvNA)
                        else -> tvLanguage.text = if (it.spokenLanguages.isNotEmpty()) {
                            it.spokenLanguages[0].englishName
                        } else {
                            it.originalLanguage
                        }
                    }

                    when {
                        it.originCountry.isEmpty() -> tvCountry.text = getString(R.string.tvNA)
                        else -> tvCountry.text =
                            it.originCountry.joinToString { countries -> countries }
                    }

                    when {
                        it.episodeRunTime.isEmpty() -> tvEpisodes.text = getString(R.string.tvNA)
                        else -> tvEpisodes.text = String.format(
                            "%s %s",
                            "${it.episodeRunTime[0]}",
                            getString(R.string.tvEps)
                        )
                    }

                    when {
                        it.genres.isEmpty() -> showNoGenres(true)
                        else -> {
                            showNoGenres(false)
                            tvGenre.text = it.genres.joinToString { genres -> genres.name }
                        }
                    }

                    checkFavoriteData(idTvShow)
                    setActionFavorite(
                        idTvShow,
                        it.posterPath,
                        title,
                        it.firstAirDate,
                        it.voteAverage
                    )
                    shareTvShow(it.homepage)
                    setCast(idTvShow)
                    showTrailerVideo(idTvShow)
                    showReviews(idTvShow)
                    showSimilar(idTvShow)
                }
            }
        }
    }

    private fun checkFavoriteData(id: Int) = with(binding) {
        CoroutineScope(Dispatchers.IO).launch {
            val count = viewModel.checkFavorite(id)
            withContext(Dispatchers.Main) {
                if (count != null) {
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
    }

    private fun setActionFavorite(
        id: Int,
        posterPath: String,
        title: String,
        firstAirDate: String,
        voteAverage: Double
    ) = with(binding) {
        toolbarDetailTvShow.toggleFavorite.setOnClickListener {
            isChecked = !isChecked
            if (isChecked) {
                viewModel.addToFavorite(
                    id,
                    posterPath,
                    title,
                    firstAirDate,
                    voteAverage
                )
                showToasty(requireContext(), getString(R.string.action_added_to_favorite))
            } else {
                viewModel.removeFromFavorite(id)
                showToasty(requireContext(), getString(R.string.action_removed_from_favorite))
            }
        }
    }

    private fun shareTvShow(link: String) = with(binding) {
        toolbarDetailTvShow.imgShare.setOnClickListener {
            try {
                val body = getString(R.string.tvVisitTvShow, link)
                val shareIntent = Intent(Intent.ACTION_SEND)
                shareIntent.apply {
                    type = "text/plain"
                    putExtra(Intent.EXTRA_TEXT, body)
                }
                startActivity(Intent.createChooser(shareIntent, getString(R.string.tvShareWith)))
            } catch (e: Exception) {
                showToasty(requireContext(), getString(R.string.tvOops))
            }
        }
    }

    private fun setReadMore() = with(binding) {
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

    private fun setCastAdapter() = with(binding) {
        rvTvShowCast.apply {
            val helper: SnapHelper = LinearSnapHelper()
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            helper.attachToRecyclerView(this)
            setHasFixedSize(true)
            adapter = castAdapter
        }
    }

    private fun setCast(id: Int) {
        viewModel.apply {
            setDetailTvShowsCast(id)
            listDetailCasts.observe(viewLifecycleOwner) {
                if (it != null && it.isNotEmpty()) {
                    castAdapter.submitList(it)
                    showNoCast(false)
                } else {
                    showNoCast(true)
                }
            }
        }
    }

    private fun setTrailerVideoAdapter() = with(binding) {
        rvTrailerVideo.apply {
            val helper: SnapHelper = LinearSnapHelper()
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            helper.attachToRecyclerView(this)
            setHasFixedSize(true)
            adapter = trailerVideoAdapter
        }
    }

    private fun showTrailerVideo(id: Int) {
        viewModel.apply {
            setDetailTvShowVideo(id)
            detailVideo.observe(viewLifecycleOwner) {
                if (it != null) {
                    when {
                        it.results.isEmpty() -> showVideo(false)
                        else -> {
                            showVideo(true)
                            trailerVideoAdapter.submitList(it.results)
                        }
                    }
                }
            }
        }
    }

    private fun setReviewsAdapter() = with(binding) {
        rvTvShowReviews.apply {
            val helper: SnapHelper = LinearSnapHelper()
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            helper.attachToRecyclerView(this)
            setHasFixedSize(true)
            adapter = reviewsAdapter
        }
    }

    private fun showReviews(id: Int) = with(binding) {
        viewModel.apply {
            setDetailTvShowsReviews(id)
            listReviewTvShows.observe(viewLifecycleOwner) {
                if (it != null && it.isNotEmpty()) {
                    reviewsAdapter.submitList(it)
                    tvNoReviews.isVisible = false
                } else {
                    tvNoReviews.isVisible = true
                }
            }
        }
    }

    private fun setSimilarAdapter() = with(binding) {
        rvTvShowSimilar.apply {
            val helper: SnapHelper = LinearSnapHelper()
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            helper.attachToRecyclerView(this)
            setHasFixedSize(true)
            adapter = similarAdapter
        }
    }

    private fun showSimilar(id: Int) {
        viewModel.apply {
            setDetailTvShowsSimilar(id)
            listSimilarTvShows.observe(viewLifecycleOwner) {
                if (it != null && it.isNotEmpty()) {
                    similarAdapter.submitList(it)
                    showNoSimilarTvShow(false)
                } else {
                    showNoSimilarTvShow(true)
                }
            }
        }
    }

    private fun showVideo(isShow: Boolean) = with(binding) {
        if (isShow) {
            rvTrailerVideo.isVisible = true
            tvNoVideo.isVisible = false
        } else {
            rvTrailerVideo.isVisible = false
            tvNoVideo.isVisible = true
        }
    }

    private fun showNoGenres(isShow: Boolean) = binding.tvNoGenres.isVisible == isShow

    private fun showNoCast(isShow: Boolean) = binding.tvNoCast.isVisible == isShow

    private fun showNoSimilarTvShow(isShow: Boolean) = binding.tvNoSimilar.isVisible == isShow

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        const val EXTRA_ID = "EXTRA_ID"
        const val EXTRA_TITLE = "EXTRA_TITLE"
    }
}