package com.taufik.themovieshow.movie.viewmodel

import androidx.lifecycle.ViewModel
import com.taufik.themovieshow.data.Movie
import com.taufik.themovieshow.utils.DataDummy

class MovieViewModel : ViewModel() {

    fun getMovies() : List<Movie> = DataDummy.generateMovies()
}