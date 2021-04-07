package com.taufik.themovieshow.ui.main.movie.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.taufik.themovieshow.databinding.FragmentMovieUpcomingBinding

class MovieUpcomingFragment : Fragment() {

    private lateinit var movieUpcomingBinding: FragmentMovieUpcomingBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        movieUpcomingBinding = FragmentMovieUpcomingBinding.inflate(inflater, container, false)
        return movieUpcomingBinding.root
    }
}