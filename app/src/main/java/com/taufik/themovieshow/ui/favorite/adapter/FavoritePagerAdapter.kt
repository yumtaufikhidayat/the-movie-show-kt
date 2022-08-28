package com.taufik.themovieshow.ui.favorite.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.taufik.themovieshow.ui.favorite.fragment.FavoriteMovieFragment
import com.taufik.themovieshow.ui.favorite.fragment.FavoriteTvShowsFragment

class FavoritePagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {

    override fun createFragment(position: Int): Fragment {
        var fragment: Fragment? = null
        when (position) {
            0 -> fragment = FavoriteMovieFragment()
            1 -> fragment = FavoriteTvShowsFragment()
        }

        return fragment as Fragment
    }

    override fun getItemCount(): Int = 2
}