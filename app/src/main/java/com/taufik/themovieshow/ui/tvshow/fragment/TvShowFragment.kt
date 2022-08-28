package com.taufik.themovieshow.ui.tvshow.fragment

import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayoutMediator
import com.taufik.themovieshow.R
import com.taufik.themovieshow.databinding.FragmentTvShowBinding
import com.taufik.themovieshow.ui.discover.tvshow.DiscoverTvShowsActivity
import com.taufik.themovieshow.ui.tvshow.adapter.TvShowPagerAdapter

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

    private fun setToolbar() = with(binding) {
        toolbarTvShow.tvToolbar.text = getString(R.string.icTVShows)
    }

    private fun setTabLayout() = with(binding) {
        val mainPagerAdapter = TvShowPagerAdapter(this@TvShowFragment)
        viewPagerTvShow.adapter = mainPagerAdapter
        TabLayoutMediator(tabLayoutTvShow, viewPagerTvShow) { tabs, position ->
            tabs.text = getString(tabsTitle[position])
        }.attach()
    }

    private fun setActionClick() = with(binding) {
        fabTvShow.setOnClickListener {
            startActivity(Intent(requireActivity(), DiscoverTvShowsActivity::class.java))
        }
    }

    companion object {
        @StringRes
        private val tabsTitle = intArrayOf(R.string.tvTvShowAiringToday, R.string.tvTvShowPopular)
    }
}