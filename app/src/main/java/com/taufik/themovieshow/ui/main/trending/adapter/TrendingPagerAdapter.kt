package com.taufik.themovieshow.ui.main.trending.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.taufik.themovieshow.ui.main.movie.fragment.MovieTrendingFragment
import com.taufik.themovieshow.ui.main.tvshow.fragment.TvShowTrendingFragment

class TrendingPagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {

    private val listOfFragments = listOf(
        MovieTrendingFragment(),
        TvShowTrendingFragment()
    )

    override fun createFragment(position: Int): Fragment = listOfFragments[position]

    override fun getItemCount(): Int = listOfFragments.size
}