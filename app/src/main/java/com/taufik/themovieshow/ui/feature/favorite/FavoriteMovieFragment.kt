package com.taufik.themovieshow.ui.feature.favorite

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.taufik.themovieshow.databinding.FragmentFavoriteMovieBinding
import com.taufik.themovieshow.ui.feature.movie.data.local.FavoriteMovie
import com.taufik.themovieshow.ui.feature.movie.data.nowplayingupcoming.MovieMainResult
import com.taufik.themovieshow.ui.feature.movie.ui.adapter.MovieAdapter
import com.taufik.themovieshow.ui.feature.movie.viewmodel.FavoriteMovieViewModel

class FavoriteMovieFragment : Fragment() {

    private lateinit var favoriteMovieBinding: FragmentFavoriteMovieBinding
    private lateinit var movieAdapter: MovieAdapter
    private lateinit var viewModel: FavoriteMovieViewModel

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

        getFavoriteMovie()
    }

    private fun setAdapter() {
        movieAdapter = MovieAdapter()
        movieAdapter.notifyDataSetChanged()
    }

    private fun setViewModel() {
        viewModel = ViewModelProvider(requireActivity())[FavoriteMovieViewModel::class.java]
    }

    private fun setData() {
        with(favoriteMovieBinding.rvDiscoverFavoriteMovies) {
            layoutManager = LinearLayoutManager(context)
            setHasFixedSize(true)
            adapter = movieAdapter
        }
    }

    private fun getFavoriteMovie() {
        viewModel.getFavoriteMovies()?.observe(viewLifecycleOwner, {
            if (it != null) {
                val list = mapList(it)
                movieAdapter.setMovies(list)
            }
        })
    }

    private fun mapList(movies: List<FavoriteMovie>):  ArrayList<MovieMainResult> {

        val listMovies = ArrayList<MovieMainResult>()

        for (movie in movies) {
            val movieMapped = MovieMainResult(
                movie.movieId,
                movie.moviePoster,
                movie.movieReleaseData,
                movie.movieTitle,
                movie.movieRating
            )
            listMovies.add(movieMapped)
        }

        return listMovies
    }
}