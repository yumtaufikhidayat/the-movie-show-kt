package com.taufik.themovieshow.ui.main.tvshow.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.taufik.themovieshow.data.NetworkResult
import com.taufik.themovieshow.data.repository.TheMovieShowRepository
import com.taufik.themovieshow.model.response.tvshow.discover.DiscoverTvShowsResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TvShowsViewModel @Inject constructor(private val repository: TheMovieShowRepository) : ViewModel() {

    private val _discoverTvShowsResponse: MutableLiveData<NetworkResult<DiscoverTvShowsResponse>> = MutableLiveData()
    val discoverTvShowsResponse: LiveData<NetworkResult<DiscoverTvShowsResponse>> get() = _discoverTvShowsResponse

    fun setTvShowsAiringToday() = repository.getTvShowsAiringToday().cachedIn(viewModelScope)

    fun setTvShowsPopular() = repository.getTvShowsPopular().cachedIn(viewModelScope)

    fun setTvShowsTrending() = repository.getTvShowsTrending().cachedIn(viewModelScope)

    fun setDiscoverTvShows(query: String) = viewModelScope.launch {
        repository.getDiscoverTvShows(query).collect {
            _discoverTvShowsResponse.value = it
        }
    }
}