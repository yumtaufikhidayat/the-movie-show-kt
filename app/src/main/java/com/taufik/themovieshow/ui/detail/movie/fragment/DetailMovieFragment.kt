package com.taufik.themovieshow.ui.detail.movie.fragment

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
import com.taufik.themovieshow.data.viewmodel.movie.DetailMovieViewModel
import com.taufik.themovieshow.databinding.FragmentDetailMovieBinding
import com.taufik.themovieshow.ui.detail.movie.adapter.MovieTrailerVideoAdapter
import com.taufik.themovieshow.ui.main.movie.adapter.MovieCastAdapter
import com.taufik.themovieshow.ui.main.movie.adapter.MovieSimilarAdapter
import com.taufik.themovieshow.ui.main.movie.adapter.ReviewsAdapter
import com.taufik.themovieshow.utils.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DetailMovieFragment : Fragment() {

    private var _binding: FragmentDetailMovieBinding? = null
    private val binding get() = _binding!!

    private val viewModel: DetailMovieViewModel by viewModels()
    private val castAdapter by lazy { MovieCastAdapter() }
    private val trailerVideoAdapter by lazy { MovieTrailerVideoAdapter() }
    private val reviewsAdapter by lazy { ReviewsAdapter() }
    private val similarAdapter by lazy { MovieSimilarAdapter() }

    private var idMovie = 0
    private var title = ""
    private var isChecked = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentDetailMovieBinding.inflate(inflater, container, false)
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
        setSimilarMovieAdapter()
    }

    private fun getBundleData() {
        idMovie = arguments?.getInt(EXTRA_ID, 0) ?: 0
        title = arguments?.getString(EXTRA_TITLE, "") ?: ""
    }

    private fun showToolbarData() = with(binding) {
        toolbarDetailMovie.apply {
            tvToolbar.text = title
            imgBack.setOnClickListener {
                findNavController().popBackStack()
            }
        }
    }

    private fun setData() = with(binding) {
        viewModel.apply {
            setDetailMovies(idMovie)
            detailMovies.observe(viewLifecycleOwner) {
                if (it != null) {
                    imgPoster.loadImage(it.posterPath)
                    imgBackdrop.loadImage(it.backdropPath)
                    tvTitle.text = it.title
                    val releasedDate =  it.releaseDate.convertDate(
                        CommonDateFormatConstants.YYYY_MM_DD_FORMAT,
                        CommonDateFormatConstants.EEE_D_MMM_YYYY_FORMAT
                    )
                    tvReleasedOn.text = String.format("%s %s", getString(R.string.tvReleasedOn), releasedDate)

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
                        it.voteAverage.toString().isEmpty() -> tvRating.text = getString(R.string.tvNA)
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
                        it.productionCountries.isEmpty() -> tvCountry.text = getString(R.string.tvNA)
                        else -> tvCountry.text = it.productionCountries.joinToString { countries -> countries.iso31661 }
                    }

                    when {
                        it.runtime.toString().isEmpty() -> tvRuntime.text = getString(R.string.tvNA)
                        else -> tvRuntime.text = convertRuntime(it.runtime)
                    }

                    when {
                        it.genres.isEmpty() -> showNoGenres(true)
                        else -> {
                            showNoGenres(false)
                            tvGenre.text = it.genres.joinToString { genre -> genre.name }
                        }
                    }

                    checkFavoriteData(idMovie)
                    setActionFavorite(idMovie, it.posterPath, title, it.releaseDate, it.voteAverage)
                    shareMovie(it.homepage)
                    setCast(idMovie)
                    showTrailerVideo(idMovie)
                    showReviews(idMovie)
                    showSimilar(idMovie)
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
                        toolbarDetailMovie.toggleFavorite.isChecked = true
                        isChecked = true
                    } else {
                        toolbarDetailMovie.toggleFavorite.isChecked = false
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
        releaseDate: String,
        voteAverage: Double
    ) = with(binding) {
        toolbarDetailMovie.toggleFavorite.setOnClickListener {
            isChecked = !isChecked
            if (isChecked) {
                viewModel.addToFavorite(
                    id,
                    posterPath,
                    title,
                    releaseDate,
                    voteAverage
                )
                showToasty(requireContext(), getString(R.string.action_added_to_favorite))
            } else {
                viewModel.removeFromFavorite(id)
                showToasty(requireContext(), getString(R.string.action_removed_from_favorite))
            }
        }
    }

    private fun shareMovie(link: String) = with(binding) {
        toolbarDetailMovie.imgShare.setOnClickListener {
            try {
                val body = getString(R.string.tvVisitMovie, link)
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
        rvMovieCast.apply {
            val helper: SnapHelper = LinearSnapHelper()
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            helper.attachToRecyclerView(this)
            setHasFixedSize(true)
            adapter = castAdapter
        }
    }

    private fun setCast(id: Int) {
        viewModel.apply {
            setDetailMovieCast(id)
            listDetailCast.observe(viewLifecycleOwner) {
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
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            helper.attachToRecyclerView(this)
            setHasFixedSize(true)
            adapter = trailerVideoAdapter
        }
    }

    private fun showTrailerVideo(id: Int) {
        viewModel.apply {
            setDetailMovieVideo(id)
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
        rvMovieReviews.apply {
            val helper: SnapHelper = LinearSnapHelper()
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            helper.attachToRecyclerView(this)
            setHasFixedSize(true)
            adapter = reviewsAdapter
        }
    }

    private fun showReviews(id: Int) = with(binding) {
        viewModel.apply {
            setDetailMovieReviews(id)
            listReviewMovie.observe(viewLifecycleOwner) {
                if (it != null && it.isNotEmpty()) {
                    reviewsAdapter.submitList(it)
                    tvNoReviews.isVisible = false
                } else {
                    tvNoReviews.isVisible = true
                }
            }
        }
    }

    private fun setSimilarMovieAdapter() = with(binding) {
        rvMovieSimilar.apply {
            val helper: SnapHelper = LinearSnapHelper()
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            helper.attachToRecyclerView(this)
            setHasFixedSize(true)
            adapter = similarAdapter
        }
    }

    private fun showSimilar(id: Int) {
        viewModel.apply {
            setDetailMovieSimilar(id)
            listSimilarMovie.observe(viewLifecycleOwner) {
                if (it != null && it.isNotEmpty()) {
                    similarAdapter.submitList(it)
                    showNoSimilarMovie(false)
                } else {
                    showNoSimilarMovie(true)
                }
            }
        }
    }

    private fun convertRuntime(data: Int): String {
        val hours = data / TIME_60
        val minutes = data % TIME_60
        return getString(R.string.tvRuntimeInTime, hours, minutes)
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

    private fun showNoSimilarMovie(isShow: Boolean) = binding.tvNoSimilar.isVisible == isShow

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        const val EXTRA_ID = "EXTRA_ID"
        const val EXTRA_TITLE = "EXTRA_TITLE"
        const val TIME_60 = 60
    }
}