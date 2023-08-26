package com.taufik.themovieshow.ui.tvshow.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
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

    fun getTvShowsAiringToday() = repository.getTvShowsAiringToday().asLiveData()

    fun getTvShowsPopular() = repository.getTvShowsPopular().asLiveData()

    fun getTvShowsTrending() = repository.getTvShowsTrending().asLiveData()

    fun setDiscoverTvShows(query: String) = viewModelScope.launch {
        repository.getDiscoverTvShows(query).collect {
            _discoverTvShowsResponse.value = it
        }
    }
}