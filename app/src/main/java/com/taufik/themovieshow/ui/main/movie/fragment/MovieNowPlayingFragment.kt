package com.taufik.themovieshow.ui.main.movie.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.taufik.themovieshow.data.NetworkResult
import com.taufik.themovieshow.databinding.FragmentMovieNowPlayingBinding
import com.taufik.themovieshow.ui.main.movie.adapter.MovieAdapter
import com.taufik.themovieshow.ui.main.movie.viewmodel.MovieViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MovieNowPlayingFragment : Fragment() {

    private var _binding: FragmentMovieNowPlayingBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<MovieViewModel>()
    private val movieAdapter by lazy { MovieAdapter() }

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
        viewModel.apply {
            setMovieNowPlaying()
            movieNowPlayingResponse.observe(viewLifecycleOwner) { response ->
                when (response) {
                    is NetworkResult.Loading -> showLoading(true)
                    is NetworkResult.Success -> {
                        showLoading(false)
                        val data = response.data
                        if (data != null) movieAdapter.submitList(data.results)
                    }
                    is NetworkResult.Error -> showLoading(false)
                }
            }
        }
    }

    private fun showLoading(isShow: Boolean) = binding.progressBar.isVisible == isShow

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}