package com.taufik.themovieshow.ui.feature.tvshow.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.taufik.themovieshow.databinding.FragmentTvShowBinding
import com.taufik.themovieshow.ui.feature.tvshow.adapter.TvShowPagerAdapter

class TvShowFragment : Fragment() {

    private lateinit var tvShowFragmentBinding: FragmentTvShowBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        tvShowFragmentBinding = FragmentTvShowBinding.inflate(inflater, container, false)
        return tvShowFragmentBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setViewPager()
    }

    private fun setViewPager() {
        val mainPagerAdapter = TvShowPagerAdapter(requireContext(), childFragmentManager)
        tvShowFragmentBinding.apply {
            viewPagerTvShow.adapter = mainPagerAdapter
            tabLayoutTvShow.setupWithViewPager(viewPagerTvShow)
        }
    }
}