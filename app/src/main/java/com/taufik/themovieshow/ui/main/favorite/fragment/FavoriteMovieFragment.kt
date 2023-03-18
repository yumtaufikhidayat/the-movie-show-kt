package com.taufik.themovieshow.ui.main.favorite.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.taufik.themovieshow.R
import com.taufik.themovieshow.data.local.entity.FavoriteMovie
import com.taufik.themovieshow.data.viewmodel.movie.FavoriteMovieViewModel
import com.taufik.themovieshow.databinding.FragmentFavoriteMovieBinding
import com.taufik.themovieshow.ui.main.movie.adapter.MovieAdapter

class FavoriteMovieFragment : Fragment() {

    private var _binding: FragmentFavoriteMovieBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<FavoriteMovieViewModel>()
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
        searchData()
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
            if (it != null && it.isNotEmpty()) {
                movieAdapter.setData(mapList(it))
                showNoFavorite(false)
            } else {
                showNoFavorite(true)
            }
        }
    }

    private fun searchData() {
        binding.etSearch.apply {
            setOnEditorActionListener(TextView.OnEditorActionListener { _, actionId, _ ->
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    hideKeyboard()
                    return@OnEditorActionListener true
                }
                false
            })

            addTextChangedListener(afterTextChanged = { p0 ->
                movieAdapter.filter.filter(p0.toString())
            })
        }
    }

    private fun mapList(movies: List<FavoriteMovie>): ArrayList<com.taufik.themovieshow.model.response.movie.nowplayingupcoming.MovieMainResult> {
        val listMovies =
            ArrayList<com.taufik.themovieshow.model.response.movie.nowplayingupcoming.MovieMainResult>()
        movies.forEach { movie ->
            val movieMapped =
                com.taufik.themovieshow.model.response.movie.nowplayingupcoming.MovieMainResult(
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

    private fun hideKeyboard() = with(binding) {
        etSearch.clearFocus()
        val imm =
            requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(etSearch.windowToken, 0)
    }

    private fun showNoFavorite(isShow: Boolean) = with(binding) {
        if (isShow) {
            layoutNoFavorite.apply {
                root.isVisible = true
                imgError.setImageResource(R.drawable.ic_outline_no_favorite)
                tvError.text = getString(R.string.tvNoFavoriteData)
            }
        } else {
            layoutNoFavorite.root.isVisible = false
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}