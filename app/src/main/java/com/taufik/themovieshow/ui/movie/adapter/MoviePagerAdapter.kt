package com.taufik.themovieshow.ui.movie.adapter

import android.content.Context
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.taufik.themovieshow.R
import com.taufik.themovieshow.ui.movie.fragment.MovieNowPlayingFragment
import com.taufik.themovieshow.ui.movie.fragment.MovieUpcomingFragment

class MoviePagerAdapter(private val context: Context, fragmentManager: FragmentManager)
    : FragmentStatePagerAdapter(fragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    @StringRes
    private val tabsTitle = intArrayOf(R.string.tvMovieNowPlaying, R.string.tvMovieUpcoming)

    override fun getCount(): Int = 2

    override fun getItem(position: Int): Fragment {
        var fragment: Fragment? = null
        when (position) {
            0 -> fragment = MovieNowPlayingFragment()
            1 -> fragment = MovieUpcomingFragment()
        }

        return fragment as Fragment
    }

    override fun getPageTitle(position: Int): CharSequence {
        return context.resources.getString(tabsTitle[position])
    }
}