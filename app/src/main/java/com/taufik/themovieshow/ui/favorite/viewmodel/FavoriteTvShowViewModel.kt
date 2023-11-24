package com.taufik.themovieshow.ui.favorite.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import com.taufik.themovieshow.data.repository.TheMovieShowRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class FavoriteTvShowViewModel @Inject constructor(
    private val favoriteTvShowRepository: TheMovieShowRepository
) : ViewModel() {

    private val _getAllFavoriteTvShows = favoriteTvShowRepository.getAllFavoriteTvShows()
    val getAllFavoriteTvShows = _getAllFavoriteTvShows

    private val _getFavoriteTvShowsByTitle = favoriteTvShowRepository.getFavoriteTvShowsByTitle()
    val getFavoriteTvShowsByTitle = _getFavoriteTvShowsByTitle

    private val _getFavoriteTvShowsByRelease = favoriteTvShowRepository.getFavoriteTvShowsByRelease()
    val getFavoriteTvShowsByRelease = _getFavoriteTvShowsByRelease

    private val _getFavoriteTvShowsByRating = favoriteTvShowRepository.getFavoriteTvShowsByRating()
    val getFavoriteTvShowsByRating = _getFavoriteTvShowsByRating

    fun getSortFiltering(context: Context) = favoriteTvShowRepository.getSortFiltering(context)

    companion object {
        var position = 0
    }
}