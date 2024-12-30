package com.taufik.themovieshow.ui.favorite.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
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
import com.taufik.themovieshow.ui.movie.viewmodel.DetailMovieViewModel
import com.taufik.themovieshow.utils.extensions.hideKeyboard
import com.taufik.themovieshow.utils.extensions.navigateToDetailMovie
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class FavoriteMovieFragment : Fragment() {

    private var _binding: FragmentFavoriteMovieBinding? = null
    private val binding get() = _binding

    private val viewModel: FavoriteMovieViewModel by viewModels(ownerProducer = { requireParentFragment() })
    private val detailMovieViewModel by viewModels<DetailMovieViewModel>()
    private val sortFilteringAdapter by lazy { SortFilteringAdapter { showFilteringData(it) }}
    private val favoriteMovieAdapter by lazy { FavoriteMovieAdapter {
        detailMovieViewModel.apply {
            idMovie = it.id
            titleMovie = it.title
        }
        navigateToDetailMovie(detailMovieViewModel.idMovie, detailMovieViewModel.titleMovie)
    }}

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFavoriteMovieBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setAdapter()
        searchData()
        setFilteringAdapter()
        showSortFilteringData()
        sortFilteringAdapter.setDefaultSelectedItemPosition(FavoriteMovieViewModel.position)
        showFavoriteMovieData(FavoriteMovieViewModel.position)
    }

    private fun setAdapter() {
        binding?.rvDiscoverFavoriteMovies?.apply {
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)
            adapter = favoriteMovieAdapter
        }
    }

    private fun setFilteringAdapter() {
        val flexLayoutManager = FlexboxLayoutManager(requireContext())
        flexLayoutManager.apply {
            flexDirection = FlexDirection.ROW
            justifyContent = JustifyContent.FLEX_START
        }

        binding?.rvSortFiltering?.apply {
            layoutManager = flexLayoutManager
            setHasFixedSize(true)
            isNestedScrollingEnabled = true
            adapter = sortFilteringAdapter
        }
    }

    private fun showSortFilteringData() {
        sortFilteringAdapter.submitList(viewModel.getSortFiltering())
        sortFilteringAdapter.setDefaultSelectedItemPosition(FavoriteMovieViewModel.position)
    }

    private fun showFilteringData(position: Int) {
        FavoriteMovieViewModel.position = position
        viewModel.setFavoriteOrder(position)
        scrollToTopPositionItem()
        sortFilteringAdapter.setDefaultSelectedItemPosition(FavoriteMovieViewModel.position)
    }

    private fun scrollToTopPositionItem() {
        lifecycleScope.launch {
            delay(100)
            binding?.rvDiscoverFavoriteMovies?.smoothScrollToPosition(0)
        }
    }

    private fun showFavoriteMovieData(position: Int) {
        viewModel.setFavoriteOrder(position)
        viewModel.getFavoriteMovies.observe(viewLifecycleOwner) {
            showNoFavorite(it.isEmpty())
            favoriteMovieAdapter.setData(mapList(it))
        }
    }

    private fun searchData() {
        binding?.etSearch?.apply {
            setOnEditorActionListener(TextView.OnEditorActionListener { _, actionId, _ ->
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    hideKeyboard(requireContext())
                    return@OnEditorActionListener true
                }
                false
            })

            addTextChangedListener(afterTextChanged = { p0 ->
                favoriteMovieAdapter.filter.filter(p0.toString())
            })
        }
    }

    private fun showNoFavorite(isShow: Boolean) {
        binding?.apply {
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}