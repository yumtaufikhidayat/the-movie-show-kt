package com.taufik.themovieshow.ui.favorite.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent
import com.taufik.themovieshow.R
import com.taufik.themovieshow.base.fragment.BaseFragment
import com.taufik.themovieshow.data.local.entity.movie.FavoriteMovieEntity
import com.taufik.themovieshow.databinding.FragmentFavoriteMovieBinding
import com.taufik.themovieshow.model.response.movie.nowplayingupcoming.MovieMainResult
import com.taufik.themovieshow.ui.detail.movie_tvshow.viewmodel.DetailMovieTvShowViewModel
import com.taufik.themovieshow.ui.favorite.adapter.FavoriteMovieAdapter
import com.taufik.themovieshow.ui.favorite.adapter.SortFilteringAdapter
import com.taufik.themovieshow.ui.favorite.viewmodel.FavoriteMovieViewModel
import com.taufik.themovieshow.utils.enums.FROM
import com.taufik.themovieshow.utils.extensions.hideKeyboard
import com.taufik.themovieshow.utils.extensions.navigateToDetailMovieTvShow
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class FavoriteMovieFragment : BaseFragment<FragmentFavoriteMovieBinding>() {

    private val viewModel: FavoriteMovieViewModel by viewModels(ownerProducer = { requireParentFragment() })
    private val detailMovieTvShowViewModel by viewModels<DetailMovieTvShowViewModel>()
    private val sortFilteringAdapter by lazy { SortFilteringAdapter { showFilteringData(it) }}
    private val favoriteMovieAdapter by lazy { FavoriteMovieAdapter {
        detailMovieTvShowViewModel.apply {
            idMovieTvShow = it.id
            titleMovieTvShow = it.title
        }
        navigateToDetailMovieTvShow(
            detailMovieTvShowViewModel.idMovieTvShow,
            detailMovieTvShowViewModel.titleMovieTvShow,
            FROM.MOVIE
        )
    }}

    override fun inflateBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentFavoriteMovieBinding = FragmentFavoriteMovieBinding.inflate(inflater, container, false)

    override fun onFragmentReady(savedInstanceState: Bundle?) {
        setAdapter()
        searchData()
        setFilteringAdapter()
        showSortFilteringData()
        sortFilteringAdapter.setDefaultSelectedItemPosition(FavoriteMovieViewModel.position)
        showFavoriteMovieData(FavoriteMovieViewModel.position)
    }

    private fun setAdapter() {
        binding.rvDiscoverFavoriteMovies.apply {
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

        binding.rvSortFiltering.apply {
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
            delay(DetailMovieTvShowViewModel.DELAY_SCROLL_TO_TOP_POSITION)
            binding.rvDiscoverFavoriteMovies.smoothScrollToPosition(0)
        }
    }

    private fun showFavoriteMovieData(position: Int) {
        viewModel.setFavoriteOrder(position)
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.favoriteMoviesFlow.collectLatest {
                    showNoFavorite(it.isEmpty())
                    favoriteMovieAdapter.setData(mapList(it))
                }
            }
        }
    }

    private fun searchData() {
        binding.etSearch.apply {
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
        binding.apply {
            if (isShow) {
                layoutNoFavorite.apply {
                    root.isVisible = true
                    tvErrorTitle.apply {
                        isVisible = true
                        text = getString(R.string.tvNoFavoriteMovie)
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
}