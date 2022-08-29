package com.taufik.themovieshow.ui.main.trending.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayoutMediator
import com.taufik.themovieshow.R
import com.taufik.themovieshow.databinding.FragmentTrendingBinding
import com.taufik.themovieshow.ui.main.trending.adapter.TrendingPagerAdapter

class TrendingFragment : Fragment() {

    private var _binding: FragmentTrendingBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        // Inflate the layout for this fragment
        _binding = FragmentTrendingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setToolbar()
        setTabLayout()
    }

    private fun setToolbar() = with(binding) {
        toolbarTrending.tvToolbar.text = getString(R.string.icTrending)
    }

    private fun setTabLayout() = with(binding) {
        val mainPagerAdapter = TrendingPagerAdapter(this@TrendingFragment)
        viewPagerTrending.adapter = mainPagerAdapter
        TabLayoutMediator(tabLayoutTrending, viewPagerTrending) { tabs, position ->
            tabs.text = getString(tabsTitle[position])
        }.attach()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        @StringRes
        private val tabsTitle = intArrayOf(R.string.tvMovies, R.string.tvShows)
    }
}