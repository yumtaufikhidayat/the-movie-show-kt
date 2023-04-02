package com.taufik.themovieshow.ui.main.movie.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.taufik.themovieshow.data.NetworkResult
import com.taufik.themovieshow.data.repository.TheMovieShowRepository
import com.taufik.themovieshow.model.response.movie.discover.DiscoverMovieResponse
import com.taufik.themovieshow.model.response.movie.nowplayingupcoming.MovieMainResponse
import com.taufik.themovieshow.model.response.movie.trending.MovieTrendingResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MovieViewModel @Inject constructor(private val repository: TheMovieShowRepository) : ViewModel() {

    private val _movieNowPlayingResponse: MutableLiveData<NetworkResult<MovieMainResponse>> = MutableLiveData()
    val movieNowPlayingResponse: LiveData<NetworkResult<MovieMainResponse>> = _movieNowPlayingResponse

    private val _movieUpcomingResponse: MutableLiveData<NetworkResult<MovieMainResponse>> = MutableLiveData()
    val movieUpcomingResponse: LiveData<NetworkResult<MovieMainResponse>> = _movieUpcomingResponse

    private val _movieTrendingDayResponse: MutableLiveData<NetworkResult<MovieTrendingResponse>> = MutableLiveData()
    val movieTrendingDayResponse: LiveData<NetworkResult<MovieTrendingResponse>> = _movieTrendingDayResponse

    private val _discoverMovieResponse: MutableLiveData<NetworkResult<DiscoverMovieResponse>> = MutableLiveData()
    val discoverMovieResponse: LiveData<NetworkResult<DiscoverMovieResponse>> = _discoverMovieResponse
    fun setMovieNowPlaying() = viewModelScope.launch {
        repository.getMovieNowPlaying().collect {
            _movieNowPlayingResponse.value = it
        }
    }

    fun setMovieUpcoming() = viewModelScope.launch {
        repository.getMovieUpcoming().collect {
            _movieUpcomingResponse.value = it
        }
    }

    fun setMovieTrendingDay() = viewModelScope.launch {
        repository.getMovieTrendingDay().collect {
            _movieTrendingDayResponse.value = it
        }
    }

    fun setDiscoverMovie(query: String) = viewModelScope.launch {
        repository.getDiscoverMovie(query).collect {
            _discoverMovieResponse.value = it
        }
    }
}