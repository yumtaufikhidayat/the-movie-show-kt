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
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent
import com.taufik.themovieshow.R
import com.taufik.themovieshow.data.local.entity.tvshow.FavoriteTvShowEntity
import com.taufik.themovieshow.databinding.FragmentFavoriteTvShowsBinding
import com.taufik.themovieshow.model.response.tvshow.popularairingtoday.TvShowsMainResult
import com.taufik.themovieshow.ui.favorite.adapter.FavoriteTvShowsAdapter
import com.taufik.themovieshow.ui.favorite.adapter.SortFilteringAdapter
import com.taufik.themovieshow.ui.favorite.viewmodel.FavoriteTvShowViewModel
import com.taufik.themovieshow.utils.navigateToDetailTvShow
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class FavoriteTvShowsFragment : Fragment() {

    private var _binding: FragmentFavoriteTvShowsBinding? = null
    private val binding get() = _binding!!

    private val viewModel: FavoriteTvShowViewModel by viewModels()
    private val sortFilteringAdapter by lazy { SortFilteringAdapter { showFilteringData(it)} }
    private val favoriteTvShowsAdapter by lazy { FavoriteTvShowsAdapter {
        navigateToDetailTvShow(it.id, it.name, FavoriteTvShowViewModel.position)
    }}

    private var favoriteList: List<FavoriteTvShowEntity> = listOf()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavoriteTvShowsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setAdapter()
        showFavoriteTvShowsByPosition(FavoriteTvShowViewModel.position)
        searchData()
        setFilteringAdapter()
        showSortFilteringData()
    }

    private fun setAdapter() {
        binding.rvDiscoverFavoriteTvShow.apply {
            layoutManager = LinearLayoutManager(context)
            setHasFixedSize(true)
            adapter = favoriteTvShowsAdapter
        }
    }

    private fun showFavoriteTvShowsByPosition(position: Int) {
        when (position) {
            0 -> getAllFavoriteTvShows(FavoriteTvShowViewModel.position)
            1 -> getFavoriteTvShowsByTitle(FavoriteTvShowViewModel.position)
            2 -> getFavoriteTvShowsByRelease(FavoriteTvShowViewModel.position)
            3 -> getFavoriteTvShowsByRating(FavoriteTvShowViewModel.position)
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
        sortFilteringAdapter.submitList(viewModel.getSortFiltering(requireContext()))
        sortFilteringAdapter.setDefaultSelectedItemPosition(FavoriteTvShowViewModel.position)
    }

    private fun showFilteringData(position: Int) {
        when (position) {
            0 -> {
                FavoriteTvShowViewModel.position = 0
                getAllFavoriteTvShows(FavoriteTvShowViewModel.position)
                scrollToTopPositionItem()
            }
            1 -> {
                FavoriteTvShowViewModel.position = 1
                getFavoriteTvShowsByTitle(FavoriteTvShowViewModel.position)
                scrollToTopPositionItem()
            }
            2 -> {
                FavoriteTvShowViewModel.position = 2
                getFavoriteTvShowsByRelease(FavoriteTvShowViewModel.position)
                scrollToTopPositionItem()
            }
            3 -> {
                FavoriteTvShowViewModel.position = 3
                getFavoriteTvShowsByRating(FavoriteTvShowViewModel.position)
                scrollToTopPositionItem()
            }
        }
        sortFilteringAdapter.setDefaultSelectedItemPosition(FavoriteTvShowViewModel.position)
        favoriteTvShowsAdapter.setData(mapList(favoriteList), FavoriteTvShowViewModel.position)
    }

    private fun scrollToTopPositionItem() {
        lifecycleScope.launch {
            delay(100)
            binding.rvDiscoverFavoriteTvShow.smoothScrollToPosition(0)
        }
    }

    private fun getAllFavoriteTvShows(position: Int) {
        viewModel.getAllFavoriteTvShows.observe(viewLifecycleOwner) {
            favoriteList = it
            showFavoriteTvShows(it, position)
        }
    }

    private fun getFavoriteTvShowsByTitle(position: Int) {
        viewModel.getFavoriteTvShowsByTitle.observe(viewLifecycleOwner) {
            favoriteList = it
            showFavoriteTvShows(it, position)
        }
    }

    private fun getFavoriteTvShowsByRelease(position: Int) {
        viewModel.getFavoriteTvShowsByRelease.observe(viewLifecycleOwner) {
            favoriteList = it
            showFavoriteTvShows(it, position)
        }
    }

    private fun getFavoriteTvShowsByRating(position: Int) {
        viewModel.getFavoriteTvShowsByRating.observe(viewLifecycleOwner) {
            favoriteList = it
            showFavoriteTvShows(it, position)
        }
    }

    private fun showFavoriteTvShows(favoriteTvShowList: List<FavoriteTvShowEntity>?, position: Int) {
        if (!favoriteTvShowList.isNullOrEmpty()) {
            when (position) {
                0, 1, 2, 3 -> favoriteTvShowsAdapter.setData(mapList(favoriteTvShowList), position)
            }
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
                        text = getString(R.string.tvNoFavoriteData)
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
    }

    companion object {
        const val POSITION_KEY = "position_key"
    }
}