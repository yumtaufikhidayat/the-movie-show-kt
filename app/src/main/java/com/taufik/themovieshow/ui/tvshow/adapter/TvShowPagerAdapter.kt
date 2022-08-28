package com.taufik.themovieshow.ui.tvshow.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.taufik.themovieshow.ui.tvshow.fragment.TVShowsPopularFragment
import com.taufik.themovieshow.ui.tvshow.fragment.TvShowAiringTodayFragment

class TvShowPagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {

    override fun createFragment(position: Int): Fragment {
        var fragment: Fragment? = null
        when (position) {
            0 -> fragment = TvShowAiringTodayFragment()
            1 -> fragment = TVShowsPopularFragment()
        }

        return fragment as Fragment
    }

    override fun getItemCount(): Int = 2
}