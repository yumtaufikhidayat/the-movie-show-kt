package com.taufik.themovieshow.ui.feature.trending

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.taufik.themovieshow.databinding.FragmentTrendingBinding
import com.taufik.themovieshow.ui.adapter.trending.TrendingPagerAdapter

class TrendingFragment : Fragment() {

    private lateinit var trendingFragmentBinding: FragmentTrendingBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        // Inflate the layout for this fragment
        trendingFragmentBinding = FragmentTrendingBinding.inflate(inflater, container, false)
        return trendingFragmentBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setViewPager()
    }

    private fun setViewPager() {
        val mainPagerAdapter = TrendingPagerAdapter(requireContext(), childFragmentManager)
        trendingFragmentBinding.apply {
            viewPagerTrending.adapter = mainPagerAdapter
            tabLayoutTrending.setupWithViewPager(viewPagerTrending)
        }
    }
}