package com.taufik.themovieshow.ui.movie.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayoutMediator
import com.taufik.themovieshow.R
import com.taufik.themovieshow.databinding.FragmentMovieBinding
import com.taufik.themovieshow.ui.discover.movie.DiscoverMovieActivity
import com.taufik.themovieshow.ui.movie.adapter.MoviePagerAdapter

class MovieFragment : Fragment() {

    private var _binding: FragmentMovieBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentMovieBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setToolbar()
        setTabLayout()
        setActionClick()
    }

    private fun setToolbar() = with(binding) {
        toolbarMovie.tvToolbar.text = getString(R.string.icMovies)
    }

    private fun setTabLayout() = with(binding){
        val mainPagerAdapter = MoviePagerAdapter(this@MovieFragment)
        viewPagerMovie.adapter = mainPagerAdapter
        TabLayoutMediator(tabLayoutMovie, viewPagerMovie) { tabs, position ->
            tabs.text = getString(tabsTitle[position])
        }.attach()
    }

    private fun setActionClick() = with(binding) {
        fabMovie.setOnClickListener {
            startActivity(Intent(requireActivity(), DiscoverMovieActivity::class.java))
        }
    }

    companion object {
        @StringRes
        private val tabsTitle = intArrayOf(R.string.tvMovieNowPlaying, R.string.tvMovieUpcoming)
    }
}