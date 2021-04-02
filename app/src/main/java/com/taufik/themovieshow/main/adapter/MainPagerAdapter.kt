package com.taufik.themovieshow.main.adapter

import android.content.Context
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.taufik.themovieshow.R
import com.taufik.themovieshow.main.fragment.movie.MovieFragment
import com.taufik.themovieshow.main.fragment.tvshows.TVShowsFragment

class MainPagerAdapter(private val context: Context, fragmentManager: FragmentManager)
    : FragmentStatePagerAdapter(fragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    @StringRes
    private val tabsTitle = intArrayOf(R.string.tvMovie, R.string.tvShows)

    override fun getCount(): Int = 2

    override fun getItem(position: Int): Fragment {
        var fragment: Fragment? = null
        when (position) {
            0 -> fragment = MovieFragment()
            1 -> fragment = TVShowsFragment()
        }

        return fragment as Fragment
    }

    override fun getPageTitle(position: Int): CharSequence {
        return context.resources.getString(tabsTitle[position])
    }
}