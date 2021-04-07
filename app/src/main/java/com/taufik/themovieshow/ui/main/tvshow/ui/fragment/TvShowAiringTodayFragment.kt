package com.taufik.themovieshow.ui.main.tvshow.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.taufik.themovieshow.databinding.FragmentTvShowAiringTodayBinding

class TvShowAiringTodayFragment : Fragment() {

    private lateinit var tvShowAiringTodayFragmentBinding: FragmentTvShowAiringTodayBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        tvShowAiringTodayFragmentBinding = FragmentTvShowAiringTodayBinding.inflate(inflater, container, false)
        return tvShowAiringTodayFragmentBinding.root
    }
}