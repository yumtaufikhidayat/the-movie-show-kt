package com.taufik.themovieshow.ui.detail.movie.fragment

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.SnapHelper
import com.taufik.themovieshow.R
import com.taufik.themovieshow.data.NetworkResult
import com.taufik.themovieshow.databinding.FragmentDetailMovieBinding
import com.taufik.themovieshow.ui.detail.movie.adapter.MovieCastAdapter
import com.taufik.themovieshow.ui.detail.movie.adapter.MovieSimilarAdapter
import com.taufik.themovieshow.ui.detail.movie.adapter.MovieTrailerVideoAdapter
import com.taufik.themovieshow.ui.main.movie.adapter.ReviewsAdapter
import com.taufik.themovieshow.ui.main.movie.viewmodel.DetailMovieViewModel
import com.taufik.themovieshow.utils.CommonDateFormatConstants
import com.taufik.themovieshow.utils.convertDate
import com.taufik.themovieshow.utils.loadImage
import com.taufik.themovieshow.utils.navigateToDetailMovie
import com.taufik.themovieshow.utils.showToasty
import com.taufik.themovieshow.utils.toRating
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class DetailMovieFragment : Fragment() {

    private var _binding: FragmentDetailMovieBinding? = null
    private val binding get() = _binding!!

    private val viewModel: DetailMovieViewModel by viewModels()
    private val castAdapter by lazy { MovieCastAdapter() }
    private var trailerVideoAdapter: MovieTrailerVideoAdapter? = null
    private val reviewsAdapter by lazy { ReviewsAdapter() }
    private var similarAdapter: MovieSimilarAdapter? = null

    private var idMovie = 0
    private var title = ""
    private var isChecked = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
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
        title = arguments?.getString(EXTRA_TITLE).orEmpty()
    }

    private fun showToolbarData() {
        binding.apply {
            toolbarDetailMovie.apply {
                tvToolbar.text = title
                imgBack.setOnClickListener {
                    findNavController().popBackStack()
                }
            }
        }
    }

    private fun setData() {
        binding.apply {
            viewModel.apply {
                setDetailMovies(idMovie)
                detailMoviesResponse.observe(viewLifecycleOwner) { response ->
                    when (response) {
                        is NetworkResult.Loading -> {}
                        is NetworkResult.Success -> {
                            val data = response.data
                            if (data != null) {
                                imgPoster.loadImage(data.posterPath.orEmpty())
                                imgBackdrop.loadImage(data.backdropPath)
                                tvTitle.text = data.title
                                val releasedDate = data.releaseDate.convertDate(
                                    CommonDateFormatConstants.YYYY_MM_DD_FORMAT,
                                    CommonDateFormatConstants.EEE_D_MMM_YYYY_FORMAT
                                )
                                tvReleasedOn.text = String.format(
                                    "%s %s",
                                    getString(R.string.tvReleasedOn),
                                    releasedDate
                                )
                                tvStatus.text = data.status

                                when {
                                    data.overview.isEmpty() -> {
                                        tvOverview.isVisible = false
                                        tvNoOverview.isVisible = true
                                        tvReadMore.isVisible = false
                                    }

                                    else -> {
                                        tvNoOverview.isVisible = false
                                        tvOverview.apply {
                                            isVisible = true
                                            text = data.overview
                                        }
                                    }
                                }

                                when {
                                    data.voteAverage.toString().isEmpty() -> tvRating.text = getString(R.string.tvNA)
                                    else -> tvRating.text = toRating(data.voteAverage)
                                }

                                when {
                                    data.originalLanguage.isEmpty() -> tvLanguage.text = getString(R.string.tvNA)
                                    else -> tvLanguage.text =
                                        if (data.spokenLanguages.isNotEmpty()) {
                                            data.spokenLanguages[0].englishName
                                        } else {
                                            data.originalLanguage
                                        }
                                }

                                when {
                                    data.productionCountries.isEmpty() -> tvCountry.text = getString(R.string.tvNA)
                                    else -> tvCountry.text = data.productionCountries.joinToString { countries -> countries.iso31661 }
                                }

                                when {
                                    data.runtime.toString().isEmpty() -> tvRuntime.text = getString(R.string.tvNA)
                                    else -> tvRuntime.text = convertRuntime(data.runtime)
                                }

                                when {
                                    data.genres.isEmpty() -> showNoGenres(true)
                                    else -> {
                                        showNoGenres(false)
                                        tvGenre.text = data.genres.joinToString { genre -> genre.name }
                                    }
                                }

                                checkFavoriteData(idMovie)
                                setActionFavorite(
                                    idMovie,
                                    data.posterPath.orEmpty(),
                                    title,
                                    data.releaseDate,
                                    data.voteAverage
                                )
                                shareMovie(data.homepage)
                                setCast(idMovie)
                                showTrailerVideo(idMovie)
                                showReviews(idMovie)
                                showSimilar(idMovie)
                            }
                        }
                        is NetworkResult.Error -> {}
                    }
                }
            }
        }
    }

    private fun checkFavoriteData(id: Int) {
        binding.apply {
            lifecycleScope.launch {
                val count = viewModel.checkFavoriteMovie(id)
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
                    requireContext().showToasty(getString(R.string.action_added_to_favorite))
                } else {
                    viewModel.removeMovieFromFavorite(id)
                    requireContext().showToasty(getString(R.string.action_removed_from_favorite))
                }
            }
        }
    }

    private fun shareMovie(link: String) {
        binding.apply {
            toolbarDetailMovie.imgShare.setOnClickListener {
                try {
                    val body = getString(R.string.tvVisitMovie, link)
                    val shareIntent = Intent(Intent.ACTION_SEND)
                    shareIntent.apply {
                        type = "text/plain"
                        putExtra(Intent.EXTRA_TEXT, body)
                    }
                    startActivity(
                        Intent.createChooser(
                            shareIntent,
                            getString(R.string.tvShareWith)
                        )
                    )
                } catch (e: Exception) {
                    requireContext().showToasty(getString(R.string.tvOops))
                }
            }
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
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            helper.attachToRecyclerView(this)
            setHasFixedSize(true)
            adapter = castAdapter
        }
    }

    private fun setCast(id: Int) {
        viewModel.apply {
            setDetailMovieCast(id)
            detailMoviesCastResponse.observe(viewLifecycleOwner) { response ->
                when (response) {
                    is NetworkResult.Loading -> showNoCast(false)
                    is NetworkResult.Success -> {
                        val cast = response.data?.cast
                        if (cast.isNullOrEmpty()) {
                            showNoCast(true)
                        } else {
                            showNoCast(false)
                            castAdapter.submitList(cast)
                        }
                    }
                    is NetworkResult.Error -> showNoCast(false)
                }
            }
        }
    }

    private fun setTrailerVideoAdapter() {
        trailerVideoAdapter = MovieTrailerVideoAdapter {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube://${it.key}"))
            startActivity(intent)
        }
        binding.rvTrailerVideo.apply {
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
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            helper.attachToRecyclerView(this)
            setHasFixedSize(true)
            adapter = reviewsAdapter
        }
    }

    private fun showReviews(id: Int) {
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
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            helper.attachToRecyclerView(this)
            setHasFixedSize(true)
            adapter = similarAdapter
        }
    }

    private fun showSimilar(id: Int) {
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

    private fun showNoGenres(isShow: Boolean) = binding.tvNoGenres.isVisible == isShow

    private fun showNoCast(isShow: Boolean) = binding.tvNoCast.isVisible == isShow

    private fun showNoSimilarMovie(isShow: Boolean) {
        binding.apply {
            if (isShow) {
                rvMovieSimilar.isVisible = false
                tvNoSimilar.isVisible = true
            } else {
                rvMovieSimilar.isVisible = true
                tvNoSimilar.isVisible = false
            }
        }
    }

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