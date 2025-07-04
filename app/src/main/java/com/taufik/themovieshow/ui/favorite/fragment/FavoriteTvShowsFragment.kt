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
import com.taufik.themovieshow.data.local.entity.tvshow.FavoriteTvShowEntity
import com.taufik.themovieshow.databinding.FragmentFavoriteTvShowsBinding
import com.taufik.themovieshow.model.response.tvshow.popularairingtoday.TvShowsMainResult
import com.taufik.themovieshow.ui.detail.movie_tvshow.viewmodel.DetailMovieTvShowViewModel
import com.taufik.themovieshow.ui.favorite.adapter.FavoriteTvShowsAdapter
import com.taufik.themovieshow.ui.favorite.adapter.SortFilteringAdapter
import com.taufik.themovieshow.ui.favorite.viewmodel.FavoriteTvShowViewModel
import com.taufik.themovieshow.utils.enums.FROM
import com.taufik.themovieshow.utils.extensions.hideKeyboard
import com.taufik.themovieshow.utils.extensions.navigateToDetailMovieTvShow
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class FavoriteTvShowsFragment : BaseFragment<FragmentFavoriteTvShowsBinding>() {

    private val viewModel: FavoriteTvShowViewModel by viewModels(ownerProducer = { requireParentFragment() })
    private val detailMovieTvShowViewModel by viewModels<DetailMovieTvShowViewModel>()
    private val sortFilteringAdapter by lazy { SortFilteringAdapter { showFilteringData(it) } }
    private val favoriteTvShowsAdapter by lazy {
        FavoriteTvShowsAdapter {
            detailMovieTvShowViewModel.apply {
                idMovieTvShow = it.id
                titleMovieTvShow = it.name
            }
            navigateToDetailMovieTvShow(
                detailMovieTvShowViewModel.idMovieTvShow,
                detailMovieTvShowViewModel.titleMovieTvShow,
                FROM.TV_SHOW
            )
        }
    }

    override fun inflateBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentFavoriteTvShowsBinding = FragmentFavoriteTvShowsBinding.inflate(inflater, container, false)

    override fun onFragmentReady(savedInstanceState: Bundle?) {
        setAdapter()
        searchData()
        setFilteringAdapter()
        showSortFilteringData()
        sortFilteringAdapter.setDefaultSelectedItemPosition(FavoriteTvShowViewModel.position)
        showFavoriteTvShowData(FavoriteTvShowViewModel.position)
    }

    private fun setAdapter() {
        binding.rvDiscoverFavoriteTvShow.apply {
            layoutManager = LinearLayoutManager(context)
            setHasFixedSize(true)
            adapter = favoriteTvShowsAdapter
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
        sortFilteringAdapter.setDefaultSelectedItemPosition(FavoriteTvShowViewModel.position)
    }

    private fun showFilteringData(position: Int) {
        FavoriteTvShowViewModel.position = position
        viewModel.setFavoriteOrder(position)
        scrollToTopPositionItem()
        sortFilteringAdapter.setDefaultSelectedItemPosition(FavoriteTvShowViewModel.position)
    }

    private fun scrollToTopPositionItem() {
        lifecycleScope.launch {
            delay(DetailMovieTvShowViewModel.DELAY_SCROLL_TO_TOP_POSITION)
            binding.rvDiscoverFavoriteTvShow.smoothScrollToPosition(0)
        }
    }

    private fun showFavoriteTvShowData(position: Int) {
        viewModel.setFavoriteOrder(position)
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.favoriteTvShowsFlow.collectLatest {
                    showNoFavorite(it.isEmpty())
                    favoriteTvShowsAdapter.setData(mapList(it))
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
                favoriteTvShowsAdapter.filter.filter(p0.toString())
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
                        text = getString(R.string.tvNoFavoriteTvShow)
                        setTextColor(ContextCompat.getColor(requireContext(), R.color.colorOrange))
                    }
                    tvErrorDesc.apply {
                        isVisible = true
                        text = getString(R.string.tvSaveFavoriteTvShows)
                        setTextColor(ContextCompat.getColor(requireContext(), R.color.colorOrange))
                    }
                }
            } else {
                layoutNoFavorite.root.isVisible = false
            }
        }
    }

    private fun mapList(tvShows: List<FavoriteTvShowEntity>): ArrayList<TvShowsMainResult> {
        val listTvShow = arrayListOf<TvShowsMainResult>()
        tvShows.forEach { tvShow ->
            val tvShowMapped = TvShowsMainResult(
                tvShow.tvShowFirstAirDate,
                tvShow.tvShowId,
                tvShow.tvShowTitle,
                tvShow.tvShowPoster,
                tvShow.tvShowRating
            )
            listTvShow.add(tvShowMapped)
        }

        return listTvShow
    }
}