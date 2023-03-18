package com.taufik.themovieshow.ui.main.tvshow.viewmodel

import androidx.lifecycle.ViewModel
import com.taufik.themovieshow.data.repository.TheMovieShowRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class TvShowsViewModel @Inject constructor(private val repository: TheMovieShowRepository) : ViewModel() {

    suspend fun setTvShowsAiringToday() = repository.getTvShowsAiringToday()

    suspend fun setTvShowsPopular() = repository.getTvShowsPopular()

    suspend fun setTvShowsTrending() = repository.getTvShowsTrending()

    suspend fun setDiscoverTvShows(query: String) = repository.getDiscoverTvShows(query)
}