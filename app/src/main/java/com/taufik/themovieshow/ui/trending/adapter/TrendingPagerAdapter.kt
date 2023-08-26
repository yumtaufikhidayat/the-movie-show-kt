package com.taufik.themovieshow.ui.trending.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.taufik.themovieshow.ui.movie.fragment.MovieTrendingFragment
import com.taufik.themovieshow.ui.tvshow.fragment.TvShowTrendingFragment

class TrendingPagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {

    private val listOfFragments = listOf(
        MovieTrendingFragment(),
        TvShowTrendingFragment()
    )

    override fun createFragment(position: Int): Fragment = listOfFragments[position]

    override fun getItemCount(): Int = listOfFragments.size
}