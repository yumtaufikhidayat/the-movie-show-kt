package com.taufik.themovieshow.movie.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.taufik.themovieshow.databinding.FragmentMovieBinding
import com.taufik.themovieshow.movie.adapter.MovieAdapter
import com.taufik.themovieshow.movie.viewmodel.MovieViewModel

class MovieFragment : Fragment() {

    private lateinit var movieFragmentMovieBinding: FragmentMovieBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        movieFragmentMovieBinding = FragmentMovieBinding.inflate(inflater, container, false)
        return movieFragmentMovieBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setData()
    }

    private fun setData() {
        val viewModel = ViewModelProvider(requireActivity(), ViewModelProvider.NewInstanceFactory())[MovieViewModel::class.java]
        val movies = viewModel.getMovies()

        val movieAdapter = MovieAdapter()
        movieAdapter.setMovie(movies)

        with(movieFragmentMovieBinding.rvMovie) {
            layoutManager = LinearLayoutManager(context)
            setHasFixedSize(true)
            adapter = movieAdapter
        }
    }
}