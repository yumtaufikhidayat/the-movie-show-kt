package com.taufik.themovieshow.ui.main.movie.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.taufik.themovieshow.data.NetworkResult
import com.taufik.themovieshow.data.paging.movie.MovieNowPlayingPagingSource
import com.taufik.themovieshow.data.paging.movie.MovieTrendingPagingSource
import com.taufik.themovieshow.data.paging.movie.MovieUpcomingPagingSource
import com.taufik.themovieshow.data.repository.TheMovieShowRepository
import com.taufik.themovieshow.model.response.movie.discover.DiscoverMovieResponse
import com.taufik.themovieshow.model.response.movie.nowplayingupcoming.MovieMainResult
import com.taufik.themovieshow.model.response.movie.trending.MovieTrendingResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MovieViewModel @Inject constructor(private val repository: TheMovieShowRepository) : ViewModel() {

    private val _discoverMovieResponse: MutableLiveData<NetworkResult<DiscoverMovieResponse>> = MutableLiveData()
    val discoverMovieResponse: LiveData<NetworkResult<DiscoverMovieResponse>> = _discoverMovieResponse

    fun setMovieNowPlaying(): Flow<PagingData<MovieMainResult>> = Pager(PagingConfig(1)) {
        MovieNowPlayingPagingSource(repository)
    }.flow.cachedIn(viewModelScope)

    fun setMovieUpcoming(): Flow<PagingData<MovieMainResult>> = Pager(PagingConfig(1)) {
        MovieUpcomingPagingSource(repository)
    }.flow.cachedIn(viewModelScope)

    fun setMovieTrendingDay(): Flow<PagingData<MovieTrendingResult>> =  Pager(PagingConfig(1)) {
        MovieTrendingPagingSource(repository)
    }.flow.cachedIn(viewModelScope)

    fun setDiscoverMovie(query: String) = viewModelScope.launch {
        repository.getDiscoverMovie(query).collect {
            _discoverMovieResponse.value = it
        }
    }
}