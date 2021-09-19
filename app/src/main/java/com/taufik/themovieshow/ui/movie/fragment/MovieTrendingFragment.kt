package com.taufik.themovieshow.ui.movie.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.taufik.themovieshow.BuildConfig
import com.taufik.themovieshow.databinding.FragmentMovieTrendingBinding
import com.taufik.themovieshow.ui.movie.adapter.MovieTrendingAdapter
import com.taufik.themovieshow.ui.movie.viewmodel.MovieViewModel

class MovieTrendingFragment : Fragment() {

    private lateinit var movieTrendingBinding: FragmentMovieTrendingBinding
    private lateinit var viewModel: MovieViewModel
    private lateinit var movieTrendingAdapter: MovieTrendingAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        movieTrendingBinding = FragmentMovieTrendingBinding.inflate(inflater, container, false)
        return movieTrendingBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setAdapter()

        setViewModel()

        setRecyclerView()

        setData()
    }

    private fun setAdapter() {
        movieTrendingAdapter = MovieTrendingAdapter()
        movieTrendingAdapter.notifyDataSetChanged()
    }

    private fun setViewModel() {
        viewModel = ViewModelProvider(requireActivity(), ViewModelProvider.NewInstanceFactory())[MovieViewModel::class.java]
    }

    private fun setRecyclerView() {
        with(movieTrendingBinding.rvTrendingMovie) {
            layoutManager = LinearLayoutManager(context)
            setHasFixedSize(true)
            adapter = movieTrendingAdapter
        }
    }

    private fun setData() {

        showLoading(true)

        viewModel.setMovieTrendingDay(BuildConfig.API_KEY)
        viewModel.getMovieTrendingDay().observe(viewLifecycleOwner, {
            if (it != null) {
                movieTrendingAdapter.setMovies(it)
                showLoading(false)
            }
        })
    }

    private fun showLoading(state: Boolean) {

        movieTrendingBinding.apply {
            if (state) {
                progressBar.visibility = View.VISIBLE
            } else {
                progressBar.visibility = View.GONE
            }
        }
    }
}