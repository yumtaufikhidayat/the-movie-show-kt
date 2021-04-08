package com.taufik.themovieshow.ui.main.movie.viewmodel

import androidx.lifecycle.ViewModel
import com.taufik.themovieshow.ui.main.movie.data.dummymain.DummyMovieMain
import com.taufik.themovieshow.utils.DataDummy

class DummyMovieViewModel : ViewModel() {

    fun getMovieNowPlaying(): List<DummyMovieMain> = DataDummy.generateMovieNowPlaying()

    fun getMovieUpcoming(): List<DummyMovieMain> = DataDummy.generateMovieUpcoming()
}