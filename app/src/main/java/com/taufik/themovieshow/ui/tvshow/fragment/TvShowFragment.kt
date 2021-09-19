package com.taufik.themovieshow.ui.tvshow.fragment

import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import com.taufik.themovieshow.R
import com.taufik.themovieshow.databinding.FragmentTvShowBinding
import com.taufik.themovieshow.ui.discover.tvshow.DiscoverTvShowsActivity
import com.taufik.themovieshow.ui.tvshow.adapter.TvShowPagerAdapter

class TvShowFragment : Fragment() {

    private lateinit var tvShowFragmentBinding: FragmentTvShowBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setHasOptionsMenu(true)
    }

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

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {

        inflater.inflate(R.menu.main_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
            R.id.nav_discover -> {
                val intent = Intent(requireActivity(), DiscoverTvShowsActivity::class.java)
                startActivity(intent)
            }
        }
        return super.onOptionsItemSelected(item)
    }
}