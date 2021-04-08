package com.taufik.themovieshow.ui.main.tvshow.viewmodel

import androidx.lifecycle.ViewModel
import com.taufik.themovieshow.ui.main.tvshow.data.dummymain.DummyTvShowsMain
import com.taufik.themovieshow.utils.DataDummy

class DummyTvShowsViewModel : ViewModel() {

    fun getTvShowsPopular(): List<DummyTvShowsMain> = DataDummy.generateTvShowsPopular()

    fun getTvShowsAiringToday(): List<DummyTvShowsMain> = DataDummy.generateTvShowsAiringToday()
}