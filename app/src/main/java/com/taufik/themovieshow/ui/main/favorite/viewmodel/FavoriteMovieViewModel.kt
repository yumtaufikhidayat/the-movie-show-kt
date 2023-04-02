package com.taufik.themovieshow.ui.main.favorite.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.taufik.themovieshow.data.local.entity.movie.FavoriteMovie
import com.taufik.themovieshow.data.repository.TheMovieShowRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class FavoriteMovieViewModel @Inject constructor(
    private val favoriteTvShowRepository: TheMovieShowRepository
) : ViewModel() {
    fun getFavoriteMovies(): LiveData<List<FavoriteMovie>> {
        return favoriteTvShowRepository.getFavoriteMovie()
    }
}