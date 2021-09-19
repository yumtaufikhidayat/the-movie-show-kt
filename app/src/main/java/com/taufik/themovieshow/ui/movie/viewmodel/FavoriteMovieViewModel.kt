package com.taufik.themovieshow.ui.movie.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.taufik.themovieshow.ui.favorite.data.movie.FavoriteMovie
import com.taufik.themovieshow.ui.favorite.data.movie.FavoriteMovieDao
import com.taufik.themovieshow.ui.favorite.data.movie.MovieDatabase

class FavoriteMovieViewModel(application: Application) : AndroidViewModel(application) {

    private var movieDao: FavoriteMovieDao?
    private var movieDb: MovieDatabase? = MovieDatabase.getDatabase(context = application)

    init {
        movieDao = movieDb?.favoriteMovieDao()
    }

    fun getFavoriteMovies(): LiveData<List<FavoriteMovie>>? {
        return movieDao?.getFavoriteMovie()
    }
}