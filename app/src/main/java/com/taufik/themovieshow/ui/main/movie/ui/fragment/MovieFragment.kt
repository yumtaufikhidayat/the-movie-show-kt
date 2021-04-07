package com.taufik.themovieshow.ui.main.movie.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.taufik.themovieshow.databinding.FragmentMovieBinding
import com.taufik.themovieshow.ui.main.adapter.movie.MoviePagerAdapter

class MovieFragment : Fragment() {

    private lateinit var movieFragmentBinding: FragmentMovieBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        movieFragmentBinding = FragmentMovieBinding.inflate(inflater, container, false)
        return movieFragmentBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setViewPager()
    }

    private fun setViewPager() {
        val mainPagerAdapter = MoviePagerAdapter(requireContext(), childFragmentManager)
        movieFragmentBinding.apply {
            viewPagerMovie.adapter = mainPagerAdapter
            tabLayoutMovie.setupWithViewPager(viewPagerMovie)
        }
    }
}