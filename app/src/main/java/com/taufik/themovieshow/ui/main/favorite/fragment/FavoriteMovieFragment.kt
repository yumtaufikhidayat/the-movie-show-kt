package com.taufik.themovieshow.ui.main.favorite.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.taufik.themovieshow.data.local.entity.FavoriteMovie
import com.taufik.themovieshow.data.main.movie.nowplayingupcoming.MovieMainResult
import com.taufik.themovieshow.data.viewmodel.movie.FavoriteMovieViewModel
import com.taufik.themovieshow.databinding.FragmentFavoriteMovieBinding
import com.taufik.themovieshow.ui.main.movie.adapter.MovieAdapter

class FavoriteMovieFragment : Fragment() {

    private var _binding: FragmentFavoriteMovieBinding? = null
    private val binding get() = _binding!!

    private val viewModel: FavoriteMovieViewModel by viewModels()
    private val movieAdapter by lazy { MovieAdapter() }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentFavoriteMovieBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setAdapter()
        getFavoriteMovie()
    }

    private fun setAdapter() = with(binding) {
        rvDiscoverFavoriteMovies.apply {
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)
            adapter = movieAdapter
        }
    }

    private fun getFavoriteMovie() {
        viewModel.getFavoriteMovies()?.observe(viewLifecycleOwner) {
            if (it != null) {
                movieAdapter.submitList(mapList(it))
            }
        }
    }

    private fun mapList(movies: List<FavoriteMovie>):  ArrayList<MovieMainResult> {
        val listMovies = ArrayList<MovieMainResult>()
        movies.forEach {  movie ->
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}