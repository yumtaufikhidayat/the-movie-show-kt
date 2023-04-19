package com.taufik.themovieshow.ui.main.favorite.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.taufik.themovieshow.ui.main.favorite.fragment.FavoriteMovieFragment
import com.taufik.themovieshow.ui.main.favorite.fragment.FavoriteTvShowsFragment

class FavoritePagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {

    private val listOfFragments = listOf(
        FavoriteMovieFragment(),
        FavoriteTvShowsFragment()
    )

    override fun createFragment(position: Int): Fragment = listOfFragments[position]

    override fun getItemCount(): Int = listOfFragments.size
}