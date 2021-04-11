package com.taufik.themovieshow.ui.feature.discover

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.taufik.themovieshow.databinding.FragmentFavoriteBinding
import com.taufik.themovieshow.ui.adapter.discover.DiscoverPagerAdapter

class DiscoverFragment : Fragment() {

    private lateinit var favoriteFragmentBinding: FragmentFavoriteBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        favoriteFragmentBinding = FragmentFavoriteBinding.inflate(inflater, container, false)
        return favoriteFragmentBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        setViewPager()
    }

    private fun setViewPager() {
        val discoverPagerAdapter = DiscoverPagerAdapter(requireContext(), childFragmentManager)
        favoriteFragmentBinding.apply {
            viewPagerDiscover.adapter = discoverPagerAdapter
            tabLayoutDiscover.setupWithViewPager(viewPagerDiscover)
        }
    }
}