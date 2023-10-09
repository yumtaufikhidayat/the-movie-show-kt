package com.taufik.themovieshow.ui.common.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class TabPagerAdapter(
    private val listOfFragments: List<Fragment>,
    fragment: Fragment
) : FragmentStateAdapter(fragment) {

    override fun createFragment(position: Int): Fragment = listOfFragments[position]

    override fun getItemCount(): Int = listOfFragments.size
}