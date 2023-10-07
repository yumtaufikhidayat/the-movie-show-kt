package com.taufik.themovieshow.ui.movie.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.taufik.themovieshow.ui.movie.fragment.MovieNowPlayingFragment
import com.taufik.themovieshow.ui.movie.fragment.MovieUpcomingFragment

class MoviePagerAdapter(
    private val listOfFragments: List<Fragment>,
    fragment: Fragment) : FragmentStateAdapter(fragment) {

    override fun createFragment(position: Int): Fragment = listOfFragments[position]

    override fun getItemCount(): Int = listOfFragments.size
}