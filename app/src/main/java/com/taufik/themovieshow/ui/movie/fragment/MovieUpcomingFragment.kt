package com.taufik.themovieshow.ui.movie.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.taufik.themovieshow.base.fragment.BaseFragment
import com.taufik.themovieshow.data.NetworkResult
import com.taufik.themovieshow.databinding.FragmentMovieTvShowsListBinding
import com.taufik.themovieshow.ui.detail.movie_tvshow.viewmodel.DetailMovieTvShowViewModel
import com.taufik.themovieshow.ui.movie.adapter.MovieAdapter
import com.taufik.themovieshow.ui.movie.viewmodel.MovieViewModel
import com.taufik.themovieshow.utils.enums.FROM
import com.taufik.themovieshow.utils.extensions.filterAndSortByDate
import com.taufik.themovieshow.utils.extensions.hideView
import com.taufik.themovieshow.utils.extensions.navigateToDetailMovieTvShow
import com.taufik.themovieshow.utils.extensions.showError
import com.taufik.themovieshow.utils.extensions.showView
import com.taufik.themovieshow.utils.objects.CommonDateFormatConstants
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MovieUpcomingFragment : BaseFragment<FragmentMovieTvShowsListBinding>() {

    private val viewModel by viewModels<MovieViewModel>()
    private val detailMovieTvShowViewModel by viewModels<DetailMovieTvShowViewModel>()
    private var movieAdapter: MovieAdapter? = null

    override fun inflateBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentMovieTvShowsListBinding = FragmentMovieTvShowsListBinding.inflate(inflater, container, false)

    override fun onFragmentReady(savedInstanceState: Bundle?) {
        setAdapter()
        setData()
    }

    private fun setAdapter() {
        movieAdapter = MovieAdapter {
            detailMovieTvShowViewModel.apply {
                idMovieTvShow = it.id
                titleMovieTvShow = it.title
            }
            navigateToDetailMovieTvShow(
                detailMovieTvShowViewModel.idMovieTvShow,
                detailMovieTvShowViewModel.titleMovieTvShow,
                FROM.MOVIE
            )
        }
        binding.rvCommon.apply {
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)
            adapter = movieAdapter
        }
    }

    private fun setData() {
        binding.apply {
            viewModel.getMovieUpcoming.observe(viewLifecycleOwner) {
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
        movieAdapter = null
    }
}