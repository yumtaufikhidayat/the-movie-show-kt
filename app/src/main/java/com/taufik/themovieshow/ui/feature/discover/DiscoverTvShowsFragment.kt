package com.taufik.themovieshow.ui.feature.discover

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.taufik.themovieshow.databinding.FragmentFavoriteTvShowsBinding

class DiscoverTvShowsFragment : Fragment() {

    private lateinit var favoriteTvShowBinding: FragmentFavoriteTvShowsBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        favoriteTvShowBinding = FragmentFavoriteTvShowsBinding.inflate(inflater, container, false)
        return favoriteTvShowBinding.root
    }
}