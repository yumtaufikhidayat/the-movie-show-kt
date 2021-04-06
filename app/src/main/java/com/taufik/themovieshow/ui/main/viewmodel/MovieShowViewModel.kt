package com.taufik.themovieshow.ui.main.viewmodel

import androidx.lifecycle.ViewModel
import com.taufik.themovieshow.data.MovieShow
import com.taufik.themovieshow.utils.DataDummy

class MovieShowViewModel : ViewModel() {

    fun getMovies() : List<MovieShow> = DataDummy.generateMovies()

    fun getTvShows() : List<MovieShow> = DataDummy.generateTvShow()
}