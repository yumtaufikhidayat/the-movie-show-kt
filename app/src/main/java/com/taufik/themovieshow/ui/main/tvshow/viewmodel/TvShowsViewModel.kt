package com.taufik.themovieshow.ui.main.tvshow.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.taufik.themovieshow.data.NetworkResult
import com.taufik.themovieshow.data.repository.TheMovieShowRepository
import com.taufik.themovieshow.model.response.tvshow.discover.DiscoverTvShowsResponse
import com.taufik.themovieshow.model.response.tvshow.popularairingtoday.TvShowsMainResponse
import com.taufik.themovieshow.model.response.tvshow.trending.TvShowsTrendingResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TvShowsViewModel @Inject constructor(private val repository: TheMovieShowRepository) : ViewModel() {

    private val _tvShowAiringTodayResponse: MutableLiveData<NetworkResult<TvShowsMainResponse>> = MutableLiveData()
    val tvShowAiringTodayResponse: LiveData<NetworkResult<TvShowsMainResponse>> = _tvShowAiringTodayResponse
    
    private val _tvShowPopularResponse: MutableLiveData<NetworkResult<TvShowsMainResponse>> = MutableLiveData()
    val tvShowPopularResponse: LiveData<NetworkResult<TvShowsMainResponse>> = _tvShowPopularResponse
    
    private val _tvShowTrendingResponse: MutableLiveData<NetworkResult<TvShowsTrendingResponse>> = MutableLiveData()
    val tvShowTrendingResponse: LiveData<NetworkResult<TvShowsTrendingResponse>> = _tvShowTrendingResponse
    
    private val _discoverTvShowsResponse: MutableLiveData<NetworkResult<DiscoverTvShowsResponse>> = MutableLiveData()
    val discoverTvShowsResponse: LiveData<NetworkResult<DiscoverTvShowsResponse>> = _discoverTvShowsResponse
    
    fun setTvShowsAiringToday() = viewModelScope.launch {
        repository.getTvShowsAiringToday().collect {
            _tvShowAiringTodayResponse.value = it
        }
    }

    fun setTvShowsPopular() = viewModelScope.launch {
        repository.getTvShowsPopular().collect {
            _tvShowPopularResponse.value = it
        }
    }

    fun setTvShowsTrending() = viewModelScope.launch {
        repository.getTvShowsTrending().collect {
            _tvShowTrendingResponse.value = it
        }
    }

    fun setDiscoverTvShows(query: String) = viewModelScope.launch {
        repository.getDiscoverTvShows(query).collect {
            _discoverTvShowsResponse.value = it
        }
    }
}