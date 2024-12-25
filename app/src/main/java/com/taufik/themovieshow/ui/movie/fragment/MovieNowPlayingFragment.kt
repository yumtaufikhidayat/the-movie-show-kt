package com.taufik.themovieshow.ui.movie.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.taufik.themovieshow.data.NetworkResult
import com.taufik.themovieshow.databinding.FragmentMovieTvShowsListBinding
import com.taufik.themovieshow.ui.movie.adapter.MovieAdapter
import com.taufik.themovieshow.ui.movie.viewmodel.DetailMovieViewModel
import com.taufik.themovieshow.ui.movie.viewmodel.MovieViewModel
import com.taufik.themovieshow.utils.CommonDateFormatConstants
import com.taufik.themovieshow.utils.extensions.filterAndSortByDate
import com.taufik.themovieshow.utils.extensions.hideView
import com.taufik.themovieshow.utils.extensions.navigateToDetailMovie
import com.taufik.themovieshow.utils.extensions.showError
import com.taufik.themovieshow.utils.extensions.showView
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MovieNowPlayingFragment : Fragment() {

    private var _binding: FragmentMovieTvShowsListBinding? = null
    private val binding get() = _binding

    private val viewModel by viewModels<MovieViewModel>()
    private val detailMovieViewModel by viewModels<DetailMovieViewModel>()
    private var movieAdapter: MovieAdapter? = null

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

        setAdapter()
        setData()
    }

    private fun setAdapter() {
        movieAdapter = MovieAdapter {
            detailMovieViewModel.apply {
                idMovie = it.id
                titleMovie = it.title
            }
            navigateToDetailMovie(detailMovieViewModel.idMovie, detailMovieViewModel.titleMovie)
        }

        binding?.rvCommon?.apply {
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)
            adapter = movieAdapter
        }
    }

    private fun setData() {
        binding?.apply {
            viewModel.getMovieNowPlaying.observe(viewLifecycleOwner) {
                when (it) {
                    is NetworkResult.Loading -> pbLoading.showView()
                    is NetworkResult.Success -> {
                        pbLoading.hideView()
                        val filteredAndSortedMovies = it.data?.results?.filterAndSortByDate(
                            getDate = { movie -> movie.releaseDate },
                            inputFormat = CommonDateFormatConstants.YYYY_MM_DD_FORMAT,
                            thresholdFormat = CommonDateFormatConstants.DD_MM_YYYY_FORMAT
                        )
                        movieAdapter?.submitList(filteredAndSortedMovies)
                    }

                    is NetworkResult.Error -> {
                        pbLoading.hideView()
                        layoutError.showError(it.message)
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        movieAdapter = null
    }
}