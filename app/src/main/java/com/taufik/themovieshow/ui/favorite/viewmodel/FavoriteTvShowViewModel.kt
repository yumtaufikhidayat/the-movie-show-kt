package com.taufik.themovieshow.ui.favorite.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.taufik.themovieshow.data.local.entity.tvshow.FavoriteTvShowEntity
import com.taufik.themovieshow.data.repository.TheMovieShowRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class FavoriteTvShowViewModel @Inject constructor(
    private val favoriteTvShowRepository: TheMovieShowRepository
) : ViewModel() {

    fun getFavoriteTvShow(): LiveData<List<FavoriteTvShowEntity>> {
        return favoriteTvShowRepository.getFavoriteTvShow()
    }
}