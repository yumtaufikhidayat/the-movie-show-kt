package com.taufik.themovieshow.ui.adapter.discover

import android.content.Context
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.taufik.themovieshow.R
import com.taufik.themovieshow.ui.feature.discover.DiscoverMovieFragment
import com.taufik.themovieshow.ui.feature.discover.DiscoverTvShowsFragment
import com.taufik.themovieshow.ui.feature.movie.ui.fragment.MovieNowPlayingFragment
import com.taufik.themovieshow.ui.feature.movie.ui.fragment.MovieUpcomingFragment

class DiscoverPagerAdapter(private val context: Context, fragmentManager: FragmentManager)
    : FragmentStatePagerAdapter(fragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    @StringRes
    private val tabsTitle = intArrayOf(R.string.tvMovies, R.string.tvShows)

    override fun getCount(): Int = 2

    override fun getItem(position: Int): Fragment {
        var fragment: Fragment? = null
        when (position) {
            0 -> fragment = DiscoverMovieFragment()
            1 -> fragment = DiscoverTvShowsFragment()
        }

        return fragment as Fragment
    }

    override fun getPageTitle(position: Int): CharSequence {
        return context.resources.getString(tabsTitle[position])
    }
}