package com.taufik.themovieshow.ui.feature.discover

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.taufik.themovieshow.databinding.FragmentDiscoverMovieBinding

class DiscoverMovieFragment : Fragment() {

    private lateinit var discoverMovieBinding: FragmentDiscoverMovieBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        discoverMovieBinding = FragmentDiscoverMovieBinding.inflate(inflater, container, false)
        return discoverMovieBinding.root
    }
}