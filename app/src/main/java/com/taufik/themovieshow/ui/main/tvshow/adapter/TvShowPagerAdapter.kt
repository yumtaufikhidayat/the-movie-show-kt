package com.taufik.themovieshow.ui.main.tvshow.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.taufik.themovieshow.ui.main.tvshow.fragment.TVShowsPopularFragment
import com.taufik.themovieshow.ui.main.tvshow.fragment.TvShowAiringTodayFragment

class TvShowPagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {

    private val listOfFragments = listOf(
        TvShowAiringTodayFragment(),
        TVShowsPopularFragment()
    )

    override fun createFragment(position: Int): Fragment = listOfFragments[position]

    override fun getItemCount(): Int = listOfFragments.size
}