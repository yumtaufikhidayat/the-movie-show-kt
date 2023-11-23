package com.taufik.themovieshow.ui.favorite.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import com.taufik.themovieshow.data.repository.TheMovieShowRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class FavoriteMovieViewModel @Inject constructor(
    private val favoriteTvShowRepository: TheMovieShowRepository
) : ViewModel() {

    private val _getFavoriteMovies = favoriteTvShowRepository.getFavoriteMovie()
    val getFavoriteMovies = _getFavoriteMovies

    private val _getFavoriteMoviesByTitle = favoriteTvShowRepository.getFavoriteMoviesByTitle()
    val getFavoriteMoviesByTitle = _getFavoriteMoviesByTitle

    private val _getFavoriteMoviesByRelease = favoriteTvShowRepository.getFavoriteMoviesByRelease()
    val getFavoriteMoviesByRelease = _getFavoriteMoviesByRelease

    private val _getFavoriteMoviesByRating = favoriteTvShowRepository.getFavoriteMoviesByRating()
    val getFavoriteMoviesByRating = _getFavoriteMoviesByRating

    fun getSortFiltering(context: Context) = favoriteTvShowRepository.getSortFiltering(context)

    companion object {
        var position: Int = 0
    }
}