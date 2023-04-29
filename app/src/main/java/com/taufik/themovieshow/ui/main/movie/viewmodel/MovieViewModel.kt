package com.taufik.themovieshow.ui.main.movie.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.taufik.themovieshow.data.NetworkResult
import com.taufik.themovieshow.data.repository.TheMovieShowRepository
import com.taufik.themovieshow.model.response.movie.discover.DiscoverMovieResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MovieViewModel @Inject constructor(private val repository: TheMovieShowRepository) : ViewModel() {

    private val _discoverMovieResponse: MutableLiveData<NetworkResult<DiscoverMovieResponse>> = MutableLiveData()
    val discoverMovieResponse: LiveData<NetworkResult<DiscoverMovieResponse>> get() = _discoverMovieResponse

    fun setMovieTrendingDay() = repository.getMovieTrendingDay().cachedIn(viewModelScope)

    fun setMovieNowPlaying() = repository.getMovieNowPlaying().cachedIn(viewModelScope)

    fun setMovieUpcoming() = repository.getMovieUpcoming().cachedIn(viewModelScope)

    fun setDiscoverMovie(query: String) = viewModelScope.launch {
        repository.getDiscoverMovie(query).collect {
            _discoverMovieResponse.value = it
        }
    }
}