package com.taufik.themovieshow.ui.movie.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.taufik.themovieshow.R
import com.taufik.themovieshow.data.NetworkResult
import com.taufik.themovieshow.databinding.FragmentMovieTvShowsListBinding
import com.taufik.themovieshow.ui.movie.adapter.MovieTrendingAdapter
import com.taufik.themovieshow.ui.movie.viewmodel.DetailMovieViewModel
import com.taufik.themovieshow.ui.movie.viewmodel.MovieViewModel
import com.taufik.themovieshow.utils.CommonDateFormatConstants
import com.taufik.themovieshow.utils.filterAndSortByDate
import com.taufik.themovieshow.utils.navigateToDetailMovie
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MovieTrendingFragment : Fragment() {

    private var _binding: FragmentMovieTvShowsListBinding? = null
    private val binding get() = _binding

    private val viewModel by viewModels<MovieViewModel>()
    private val detailMovieViewModel by viewModels<DetailMovieViewModel>()
    private var movieTrendingAdapter: MovieTrendingAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMovieTvShowsListBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initAdapter()
        setMovieTrendingObserver()
    }

    private fun initAdapter() {
        movieTrendingAdapter = MovieTrendingAdapter {
            detailMovieViewModel.apply {
                idMovie = it.id
                titleMovie = it.title
            }
            navigateToDetailMovie(detailMovieViewModel.idMovie, detailMovieViewModel.titleMovie)
        }

        binding?.rvCommon?.apply {
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)
            adapter = movieTrendingAdapter
        }
    }

    private fun setMovieTrendingObserver() {
        viewModel.getMovieTrendingDay.observe(viewLifecycleOwner) {
            when (it) {
                is NetworkResult.Loading -> showLoading(true)
                is NetworkResult.Success -> {
                    showLoading(false)
                    val filteredAndSortedMovies = it.data?.results?.filterAndSortByDate(
                        getDate = { movie -> movie.releaseDate },
                        dateThreshold = "01-01-2010",
                        inputFormat = CommonDateFormatConstants.YYYY_MM_DD_FORMAT,
                        thresholdFormat = CommonDateFormatConstants.DD_MM_YYYY_FORMAT
                    )
                    movieTrendingAdapter?.submitList(filteredAndSortedMovies)
                }
                is NetworkResult.Error -> {
                    showLoading(false)
                    showError(it.message)
                }
                else -> {
                    showLoading(false)
                    showError(getString(R.string.tvOops))
                }
            }
        }
    }

    private fun showLoading(isShow: Boolean) {
        binding?.pbLoading?.isVisible = isShow
    }

    private fun showError(message: String?) {
        binding?.layoutError?.apply {
            root.isVisible = true
            tvErrorDesc.text = message
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        movieTrendingAdapter = null
    }
}