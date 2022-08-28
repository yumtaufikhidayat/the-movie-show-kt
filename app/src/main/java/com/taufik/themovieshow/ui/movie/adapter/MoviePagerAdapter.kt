package com.taufik.themovieshow.ui.movie.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.taufik.themovieshow.ui.movie.fragment.MovieNowPlayingFragment
import com.taufik.themovieshow.ui.movie.fragment.MovieUpcomingFragment

class MoviePagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {

    override fun createFragment(position: Int): Fragment {
        var fragment: Fragment? = null
        when (position) {
            0 -> fragment = MovieNowPlayingFragment()
            1 -> fragment = MovieUpcomingFragment()
        }

        return fragment as Fragment
    }

    override fun getItemCount(): Int = 2
}