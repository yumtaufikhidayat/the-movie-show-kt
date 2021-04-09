package com.taufik.themovieshow.ui.feature.movie.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.taufik.themovieshow.BuildConfig
import com.taufik.themovieshow.databinding.FragmentMovieNowPlayingBinding
import com.taufik.themovieshow.ui.feature.movie.ui.adapter.MovieAdapter
import com.taufik.themovieshow.ui.feature.movie.viewmodel.MovieViewModel

class MovieNowPlayingFragment : Fragment() {

    private lateinit var movieFragmentMovieBinding: FragmentMovieNowPlayingBinding
    private lateinit var viewModel: MovieViewModel
    private lateinit var movieAdapter: MovieAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        movieFragmentMovieBinding = FragmentMovieNowPlayingBinding.inflate(inflater, container, false)
        return movieFragmentMovieBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setAdapter()

        setViewModel()

        setRecyclerView()

        setData()
    }

    private fun setAdapter() {
        movieAdapter = MovieAdapter()
        movieAdapter.notifyDataSetChanged()
    }

    private fun setViewModel() {
        viewModel = ViewModelProvider(requireActivity(), ViewModelProvider.NewInstanceFactory())[MovieViewModel::class.java]
    }

    private fun setRecyclerView() {
        with(movieFragmentMovieBinding.rvMovie) {
            layoutManager = LinearLayoutManager(context)
            setHasFixedSize(true)
            adapter = movieAdapter
        }
    }

    private fun setData() {

        showLoading(true)

        viewModel.setMovieNowPlaying(BuildConfig.API_KEY)
        viewModel.getMovieNowPlaying().observe(viewLifecycleOwner, {
            if (it != null) {
                movieAdapter.setMovies(it)
                showLoading(false)
            }
        })
    }

    private fun showLoading(state: Boolean) {

        movieFragmentMovieBinding.apply {
            if (state) {
                progressBar.visibility = View.VISIBLE
            } else {
                progressBar.visibility = View.GONE
            }
        }
    }
}