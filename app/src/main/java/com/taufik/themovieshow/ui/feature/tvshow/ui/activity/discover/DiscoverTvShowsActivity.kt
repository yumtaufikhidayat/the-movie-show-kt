package com.taufik.themovieshow.ui.feature.tvshow.ui.activity.discover

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.taufik.themovieshow.databinding.ActivityDiscoverTvShowsBinding

class DiscoverTvShowsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDiscoverTvShowsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDiscoverTvShowsBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}