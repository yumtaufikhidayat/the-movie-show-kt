package com.taufik.themovieshow.ui.favorite.viewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.taufik.themovieshow.data.local.entity.movie.FavoriteMovieEntity
import com.taufik.themovieshow.data.repository.TheMovieShowRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class FavoriteMovieViewModel @Inject constructor(
    private val favoriteTvShowRepository: TheMovieShowRepository
) : ViewModel() {
    fun getFavoriteMovies(): LiveData<List<FavoriteMovieEntity>> {
        return favoriteTvShowRepository.getFavoriteMovie()
    }

    fun getSortFiltering(context: Context) = favoriteTvShowRepository.getSortFiltering(context)
}