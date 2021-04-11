package com.taufik.themovieshow.ui.feature.discover

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.taufik.themovieshow.BuildConfig
import com.taufik.themovieshow.databinding.FragmentDiscoverMovieBinding
import com.taufik.themovieshow.ui.feature.movie.ui.adapter.SearchMovieAdapter
import com.taufik.themovieshow.ui.feature.movie.viewmodel.MovieViewModel
import es.dmoral.toasty.Toasty

class DiscoverMovieFragment : Fragment() {

    private lateinit var discoverMovieBinding: FragmentDiscoverMovieBinding
    private lateinit var viewModel: MovieViewModel
    private lateinit var movieAdapter: SearchMovieAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        discoverMovieBinding = FragmentDiscoverMovieBinding.inflate(inflater, container, false)
        return discoverMovieBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setAdapter()

        setViewModel()

        setData()

        searchMovies()
    }

    private fun setAdapter() {

        movieAdapter = SearchMovieAdapter()
        movieAdapter.notifyDataSetChanged()
    }

    private fun setViewModel() {
        viewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory())[MovieViewModel::class.java]
    }

    private fun setData() {
        with(discoverMovieBinding.rvDiscoverMovies) {
            layoutManager = LinearLayoutManager(context)
            setHasFixedSize(true)
            adapter = movieAdapter
        }
    }

    private fun showLoading(state: Boolean) {

        if (state) {
            discoverMovieBinding.progressBar.visibility = View.VISIBLE
        } else {
            discoverMovieBinding.progressBar.visibility = View.GONE
        }
    }

    private fun searchMovies() {
        discoverMovieBinding.apply {

            showLoading(true)

            searchMovies.setOnQueryTextListener(object : SearchView.OnQueryTextListener,
                    androidx.appcompat.widget.SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String): Boolean {
                    if (query.isNotEmpty()) {
                        setDiscoverMovies(query)
                    } else {
                        setDiscoverMovies("")
                    }
                    return true
                }

                override fun onQueryTextChange(newText: String): Boolean = false
            })
        }
    }

    private fun setDiscoverMovies(query: String) {

        if (query.isEmpty()) {
            Toasty.warning(requireActivity(), "Please fill discover form", Toast.LENGTH_SHORT, true).show()
        }

        viewModel.setDiscoverMovie(BuildConfig.API_KEY, query)
        viewModel.getDiscoverMovie().observe(viewLifecycleOwner, {
            if (it != null) {
                movieAdapter.setMovies(it)
                showLoading(false)
            }
        })
    }
}