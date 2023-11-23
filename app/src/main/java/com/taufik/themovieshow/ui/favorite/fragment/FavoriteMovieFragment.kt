package com.taufik.themovieshow.ui.favorite.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent
import com.taufik.themovieshow.R
import com.taufik.themovieshow.data.local.entity.movie.FavoriteMovieEntity
import com.taufik.themovieshow.databinding.FragmentFavoriteMovieBinding
import com.taufik.themovieshow.model.response.movie.nowplayingupcoming.MovieMainResult
import com.taufik.themovieshow.ui.favorite.adapter.FavoriteMovieAdapter
import com.taufik.themovieshow.ui.favorite.adapter.SortFilteringAdapter
import com.taufik.themovieshow.ui.favorite.viewmodel.FavoriteMovieViewModel
import com.taufik.themovieshow.utils.navigateToDetailMovie
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FavoriteMovieFragment : Fragment() {

    private var _binding: FragmentFavoriteMovieBinding? = null
    private val binding get() = _binding!!

    private val viewModel: FavoriteMovieViewModel by viewModels()
    private var favoriteMovieAdapter: FavoriteMovieAdapter? = null
    private var sortFilteringAdapter: SortFilteringAdapter? = null

    private var isShow: Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavoriteMovieBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setAdapter()
        getFavoriteMovie()
        searchData()
        showHideSortFilter()
        setFilteringAdapter()
        sortFilteringData()
    }

    private fun setAdapter() {
        favoriteMovieAdapter = FavoriteMovieAdapter {
            navigateToDetailMovie(it.id, it.title)
        }
        binding.rvDiscoverFavoriteMovies.apply {
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)
            adapter = favoriteMovieAdapter
        }
    }

    private fun getFavoriteMovie() {
        viewModel.getFavoriteMovies.observe(viewLifecycleOwner) {
            showFavoriteMovies(it)
        }
    }

    private fun getFavoriteMovieByTitle() {
        viewModel.getFavoriteMoviesByTitle.observe(viewLifecycleOwner) {
            showFavoriteMovies(it)
        }
    }

    private fun getFavoriteMovieByRelease() {
        viewModel.getFavoriteMoviesByRelease.observe(viewLifecycleOwner) {
            showFavoriteMovies(it)
        }
    }

    private fun getFavoriteMovieByRating() {
        viewModel.getFavoriteMoviesByRating.observe(viewLifecycleOwner) {
            showFavoriteMovies(it)
        }
    }

    private fun showFavoriteMovies(favoriteMovieList: List<FavoriteMovieEntity>?) {
        if (!favoriteMovieList.isNullOrEmpty()) {
            favoriteMovieAdapter?.setData(mapList(favoriteMovieList))
            showNoFavorite(false)
        } else {
            showNoFavorite(true)
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
                favoriteMovieAdapter?.filter?.filter(p0.toString())
            })
        }
    }

    private fun showNoFavorite(isShow: Boolean) {
        binding.apply {
            if (isShow) {
                layoutNoFavorite.apply {
                    root.isVisible = true
                    tvErrorTitle.apply {
                        isVisible = true
                        text = getString(R.string.tvNoFavoriteData)
                        setTextColor(ContextCompat.getColor(requireContext(), R.color.colorOrange))
                    }
                    tvErrorDesc.apply {
                        isVisible = true
                        text = getString(R.string.tvSaveFavoriteMovie)
                        setTextColor(ContextCompat.getColor(requireContext(), R.color.colorOrange))
                    }
                }
            } else {
                layoutNoFavorite.root.isVisible = false
            }
        }
    }

    private fun showHideSortFilter() {
        binding.imgSortFiltering.setOnClickListener {
            isShow = !isShow
            binding.groupSortFilter.isVisible = isShow
        }
    }

    private fun setFilteringAdapter() {
        sortFilteringAdapter = SortFilteringAdapter { position ->
            when (position) {
                0 -> getFavoriteMovie()
                1 -> getFavoriteMovieByTitle()
                2 -> getFavoriteMovieByRelease()
                3 -> getFavoriteMovieByRating()
                else -> getFavoriteMovie()
            }
        }

        val flexLayoutManager = FlexboxLayoutManager(requireContext())
        flexLayoutManager.apply {
            flexDirection = FlexDirection.ROW
            justifyContent = JustifyContent.FLEX_START
        }

        binding.rvSortFiltering.apply {
            layoutManager = flexLayoutManager
            setHasFixedSize(true)
            isNestedScrollingEnabled = true
            adapter = sortFilteringAdapter
        }
    }

    private fun sortFilteringData() {
        sortFilteringAdapter?.submitList(viewModel.getSortFiltering(requireContext()))
        sortFilteringAdapter?.setDefaultSelectedItemPosition(0)
    }

    private fun mapList(movies: List<FavoriteMovieEntity>): ArrayList<MovieMainResult> {
        val listMovies = ArrayList<MovieMainResult>()
        movies.forEach { movie ->
            val movieMapped =
                MovieMainResult(
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

    private fun hideKeyboard() {
        binding.apply {
            etSearch.clearFocus()
            val imm = requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(etSearch.windowToken, 0)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        favoriteMovieAdapter = null
        sortFilteringAdapter = null
    }
}