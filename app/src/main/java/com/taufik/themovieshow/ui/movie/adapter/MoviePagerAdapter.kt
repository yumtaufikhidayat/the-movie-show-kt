package com.taufik.themovieshow.ui.movie.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.taufik.themovieshow.ui.movie.fragment.MovieNowPlayingFragment
import com.taufik.themovieshow.ui.movie.fragment.MovieUpcomingFragment

class MoviePagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {

    private val listOfFragments = listOf(
        MovieNowPlayingFragment(),
        MovieUpcomingFragment()
    )

    override fun createFragment(position: Int): Fragment = listOfFragments[position]

    override fun getItemCount(): Int = listOfFragments.size
}