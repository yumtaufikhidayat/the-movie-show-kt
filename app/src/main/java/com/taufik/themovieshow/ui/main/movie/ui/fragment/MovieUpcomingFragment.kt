package com.taufik.themovieshow.ui.main.movie.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.taufik.themovieshow.BuildConfig
import com.taufik.themovieshow.databinding.FragmentMovieUpcomingBinding
import com.taufik.themovieshow.ui.main.movie.ui.adapter.MovieAdapter
import com.taufik.themovieshow.ui.main.movie.viewmodel.MovieViewModel

class MovieUpcomingFragment : Fragment() {

    private lateinit var movieUpcomingBinding: FragmentMovieUpcomingBinding
    private lateinit var viewModel: MovieViewModel
    private lateinit var movieAdapter: MovieAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        movieUpcomingBinding = FragmentMovieUpcomingBinding.inflate(inflater, container, false)
        return movieUpcomingBinding.root
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
        with(movieUpcomingBinding.rvMovie) {
            layoutManager = LinearLayoutManager(context)
            setHasFixedSize(true)
            adapter = movieAdapter
        }
    }

    private fun setData() {

        showLoading(true)

        viewModel.setMovieUpcoming(BuildConfig.API_KEY)
        viewModel.getMovieUpcoming().observe(requireActivity(), {
            if (it != null) {
                movieAdapter.setMovies(it)
                showLoading(false)
            }
        })
    }

    private fun showLoading(state: Boolean) {

        movieUpcomingBinding.apply {
            if (state) {
                progressBar.visibility = View.VISIBLE
            } else {
                progressBar.visibility = View.GONE
            }
        }
    }
}