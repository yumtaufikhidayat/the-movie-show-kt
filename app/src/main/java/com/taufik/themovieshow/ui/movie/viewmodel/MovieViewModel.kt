package com.taufik.themovieshow.ui.movie.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.taufik.themovieshow.data.NetworkResult
import com.taufik.themovieshow.data.repository.TheMovieShowRepository
import com.taufik.themovieshow.model.response.movie.discover.DiscoverMovieResponse
import com.taufik.themovieshow.model.response.movie.nowplayingupcoming.MovieMainResponse
import com.taufik.themovieshow.model.response.movie.trending.MovieTrendingResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class MovieViewModel @Inject constructor(private val repository: TheMovieShowRepository) : ViewModel() {

    fun setDiscoverMovie(query: String) : StateFlow<NetworkResult<DiscoverMovieResponse>> =
        repository.getDiscoverMovie(query).stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(DELAY_EMIT),
            initialValue = NetworkResult.Loading()
        )

    fun getMovieTrendingToday(): StateFlow<NetworkResult<MovieTrendingResponse>> =
        repository.getMovieTrendingDay().stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(DELAY_EMIT),
            initialValue = NetworkResult.Loading()
        )

    fun getMovieNowPlaying(): StateFlow<NetworkResult<MovieMainResponse>> =
        repository.getMovieNowPlaying().stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(DELAY_EMIT),
            initialValue = NetworkResult.Loading()
        )

    fun getMovieUpcoming(): StateFlow<NetworkResult<MovieMainResponse>> =
        repository.getMovieUpcoming().stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(DELAY_EMIT),
            initialValue = NetworkResult.Loading()
        )

    companion object {
        const val DELAY_EMIT = 5000L
    }
}