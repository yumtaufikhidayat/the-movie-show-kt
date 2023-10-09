package com.taufik.themovieshow.ui.movie.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.tabs.TabLayoutMediator
import com.taufik.themovieshow.R
import com.taufik.themovieshow.databinding.FragmentMovieBinding
import com.taufik.themovieshow.ui.common.adapter.TabPagerAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MovieFragment : Fragment() {

    private var _binding: FragmentMovieBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMovieBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setToolbar()
        setTabLayout()
        setActionClick()
    }

    private fun setToolbar() {
        binding.toolbarMovie.tvToolbar.text = getString(R.string.icMovies)
    }

    private fun setTabLayout() {
        val listOfFragments = listOf(
            MovieNowPlayingFragment(),
            MovieUpcomingFragment()
        )

        binding.apply {
            val mainPagerAdapter = TabPagerAdapter(listOfFragments, this@MovieFragment)
            viewPagerMovie.adapter = mainPagerAdapter
            TabLayoutMediator(tabLayoutMovie, viewPagerMovie) { tabs, position ->
                tabs.text = getString(tabsTitle[position])
            }.attach()
        }
    }

    private fun setActionClick() {
        binding.fabMovie.setOnClickListener {
            findNavController().navigate(R.id.discoverMovieFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        @StringRes
        private val tabsTitle = intArrayOf(R.string.tvMovieNowPlaying, R.string.tvMovieUpcoming)
    }
}