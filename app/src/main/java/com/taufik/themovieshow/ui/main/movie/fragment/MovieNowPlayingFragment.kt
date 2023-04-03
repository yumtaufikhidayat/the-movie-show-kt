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
import com.taufik.themovieshow.databinding.FragmentMovieNowPlayingBinding
import com.taufik.themovieshow.ui.detail.movie.fragment.DetailMovieFragment
import com.taufik.themovieshow.ui.main.LoadMoreAdapter
import com.taufik.themovieshow.ui.main.movie.adapter.MovieAdapter
import com.taufik.themovieshow.ui.main.movie.viewmodel.MovieViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MovieNowPlayingFragment : Fragment() {

    private var _binding: FragmentMovieNowPlayingBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<MovieViewModel>()
    private var movieAdapter: MovieAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMovieNowPlayingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setAdapter()
        setData()
    }

    private fun setAdapter()  {
        binding.rvMovie.apply {
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)
            adapter = movieAdapter
        }
    }

    private fun setData() {
        lifecycleScope.launch {
            movieAdapter = MovieAdapter {
                val bundle = Bundle().apply {
                    putInt(DetailMovieFragment.EXTRA_ID, it.id)
                    putString(DetailMovieFragment.EXTRA_TITLE, it.title)
                }
                findNavController().navigate(R.id.detailMovieFragment, bundle)
            }

            viewModel.setMovieNowPlaying().collect {
                movieAdapter?.submitData(it)
            }

            movieAdapter?.loadStateFlow?.collect {
                showLoading(it.refresh is LoadState.Loading)
            }
        }

        binding.rvMovie.adapter = movieAdapter?.withLoadStateFooter( LoadMoreAdapter {
            movieAdapter?.retry()
        })
    }

    private fun showLoading(isShow: Boolean) = binding.progressBar.isVisible == isShow

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        movieAdapter = null
    }
}