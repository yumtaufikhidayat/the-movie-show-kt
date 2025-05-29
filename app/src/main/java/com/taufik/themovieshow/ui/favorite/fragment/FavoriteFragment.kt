package com.taufik.themovieshow.ui.favorite.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.StringRes
import androidx.fragment.app.viewModels
import com.taufik.themovieshow.R
import com.taufik.themovieshow.base.fragment.BaseFragment
import com.taufik.themovieshow.databinding.FragmentFavoriteBinding
import com.taufik.themovieshow.ui.tablayout.FavoriteTabViewModel
import com.taufik.themovieshow.utils.extensions.setupTabLayoutBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FavoriteFragment : BaseFragment<FragmentFavoriteBinding>() {

    private val favoriteTabViewModel: FavoriteTabViewModel by viewModels()

    override fun inflateBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentFavoriteBinding = FragmentFavoriteBinding.inflate(inflater, container, false)

    override fun onFragmentReady(savedInstanceState: Bundle?) {
        setToolbar()
        setTabLayout()
    }

    private fun setToolbar() {
        binding.toolbarFavorite.tvToolbar.text = getString(R.string.icFavorite)
    }

    private fun setTabLayout() {
        binding.apply {
            setupTabLayoutBinding(
                tabLayout = tabLayoutFavorite,
                viewPager = viewPagerFavorite,
                fragments = listOf(
                    FavoriteMovieFragment(),
                    FavoriteTvShowsFragment()
                ),
                tabTitles = tabsTitle,
                selectedIndex = favoriteTabViewModel.selectedTabIndex,
                onTabChanged = { position ->
                    favoriteTabViewModel.selectedTabIndex = position
                }
            )
        }
    }

    companion object {
        @StringRes
        private val tabsTitle = intArrayOf(R.string.tvMovies, R.string.tvShows)
    }
}