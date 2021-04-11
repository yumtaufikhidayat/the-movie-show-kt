package com.taufik.themovieshow.ui.feature.favorite

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.taufik.themovieshow.databinding.FragmentFavoriteMovieBinding
import com.taufik.themovieshow.ui.feature.movie.ui.adapter.DiscoverMovieAdapter
import com.taufik.themovieshow.ui.feature.movie.viewmodel.MovieViewModel

class FavoriteMovieFragment : Fragment() {

    private lateinit var favoriteMovieBinding: FragmentFavoriteMovieBinding
    private lateinit var viewModel: MovieViewModel
    private lateinit var movieAdapter: DiscoverMovieAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        favoriteMovieBinding = FragmentFavoriteMovieBinding.inflate(inflater, container, false)
        return favoriteMovieBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setAdapter()

        setViewModel()

        setData()

        searchMovies()
    }

    private fun setAdapter() {

        movieAdapter = DiscoverMovieAdapter()
        movieAdapter.notifyDataSetChanged()
    }

    private fun setViewModel() {
        viewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory())[MovieViewModel::class.java]
    }

    private fun setData() {
        with(favoriteMovieBinding.rvDiscoverMovies) {
            layoutManager = LinearLayoutManager(context)
            setHasFixedSize(true)
            adapter = movieAdapter
        }
    }

    private fun showLoading(state: Boolean) {

        if (state) {
            favoriteMovieBinding.progressBar.visibility = View.VISIBLE
        } else {
            favoriteMovieBinding.progressBar.visibility = View.GONE
        }
    }

    private fun searchMovies() {
        favoriteMovieBinding.apply {


        }
    }
}