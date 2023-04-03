package com.taufik.themovieshow.ui.main.movie.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.taufik.themovieshow.R
import com.taufik.themovieshow.databinding.FragmentMovieTrendingBinding
import com.taufik.themovieshow.ui.detail.movie.fragment.DetailMovieFragment
import com.taufik.themovieshow.ui.main.LoadMoreAdapter
import com.taufik.themovieshow.ui.main.movie.adapter.MovieTrendingAdapter
import com.taufik.themovieshow.ui.main.movie.viewmodel.MovieViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MovieTrendingFragment : Fragment() {

    private var _binding: FragmentMovieTrendingBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<MovieViewModel>()
    private var movieTrendingAdapter: MovieTrendingAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentMovieTrendingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setAdapter()
        setData()
    }

    private fun setAdapter()  {
        binding.rvTrendingMovie.apply {
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)
            adapter = movieTrendingAdapter
        }
    }

    private fun setData() {
        lifecycleScope.launch {
            movieTrendingAdapter = MovieTrendingAdapter {
                val bundle = Bundle().apply {
                    putInt(DetailMovieFragment.EXTRA_ID, it.id)
                    putString(DetailMovieFragment.EXTRA_TITLE, it.title)
                }
                findNavController().navigate(R.id.detailMovieFragment, bundle)
            }

            viewModel.setMovieTrendingDay().collect {
                movieTrendingAdapter?.submitData(it)
            }

            movieTrendingAdapter?.loadStateFlow?.collect {
                showLoading(it.refresh is LoadState.Loading)
            }
        }

        binding.rvTrendingMovie.adapter = movieTrendingAdapter?.withLoadStateFooter( LoadMoreAdapter {
            movieTrendingAdapter?.retry()
        })
    }

    private fun showLoading(isShow: Boolean) = binding.progressBar.isVisible == isShow

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        movieTrendingAdapter = null
    }
}