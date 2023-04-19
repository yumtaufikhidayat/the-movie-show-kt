package com.taufik.themovieshow.ui.main.movie.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.liveData
import com.taufik.themovieshow.data.NetworkResult
import com.taufik.themovieshow.data.paging.movie.MovieNowPlayingPagingSource
import com.taufik.themovieshow.data.paging.movie.MovieTrendingPagingSource
import com.taufik.themovieshow.data.paging.movie.MovieUpcomingPagingSource
import com.taufik.themovieshow.data.repository.TheMovieShowRepository
import com.taufik.themovieshow.model.response.movie.discover.DiscoverMovieResponse
import com.taufik.themovieshow.utils.CommonConstants
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MovieViewModel @Inject constructor(private val repository: TheMovieShowRepository) :
    ViewModel() {

    private val _discoverMovieResponse: MutableLiveData<NetworkResult<DiscoverMovieResponse>> =
        MutableLiveData()
    val discoverMovieResponse: LiveData<NetworkResult<DiscoverMovieResponse>> =
        _discoverMovieResponse

    fun setMovieTrendingDay() = repository.getMovieTrendingDay()

    fun setMovieNowPlaying() = Pager(
        PagingConfig(
            pageSize = CommonConstants.STARTING_PAGE_INDEX,
            maxSize = CommonConstants.LOAD_PER_PAGE,
            enablePlaceholders = false
        ), pagingSourceFactory = {
            MovieNowPlayingPagingSource(repository)
        }).liveData

    fun setMovieUpcoming() = Pager(
        PagingConfig(
            pageSize = CommonConstants.STARTING_PAGE_INDEX,
            maxSize = CommonConstants.LOAD_PER_PAGE,
            enablePlaceholders = false
        ), pagingSourceFactory = {
            MovieUpcomingPagingSource(repository)
        }).liveData

    fun setDiscoverMovie(query: String) = viewModelScope.launch {
        repository.getDiscoverMovie(query).collect {
            _discoverMovieResponse.value = it
        }
    }
}