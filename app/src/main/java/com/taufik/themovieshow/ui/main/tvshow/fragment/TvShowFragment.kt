package com.taufik.themovieshow.ui.main.tvshow.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.tabs.TabLayoutMediator
import com.taufik.themovieshow.R
import com.taufik.themovieshow.databinding.FragmentTvShowBinding
import com.taufik.themovieshow.ui.main.tvshow.adapter.TvShowPagerAdapter

class TvShowFragment : Fragment() {

    private var _binding: FragmentTvShowBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentTvShowBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setToolbar()
        setTabLayout()
        setActionClick()
    }

    private fun setToolbar() {
        binding.toolbarTvShow.tvToolbar.text = getString(R.string.icTVShows)
    }

    private fun setTabLayout() {
        binding.apply {
            val mainPagerAdapter = TvShowPagerAdapter(this@TvShowFragment)
            viewPagerTvShow.adapter = mainPagerAdapter
            TabLayoutMediator(tabLayoutTvShow, viewPagerTvShow) { tabs, position ->
                tabs.text = getString(tabsTitle[position])
            }.attach()
        }
    }

    private fun setActionClick() {
        binding.fabTvShow.setOnClickListener {
            findNavController().navigate(R.id.discoverTvShowFragment)
        }
    }

    companion object {
        @StringRes
        private val tabsTitle = intArrayOf(R.string.tvTvShowAiringToday, R.string.tvTvShowPopular)
    }
}