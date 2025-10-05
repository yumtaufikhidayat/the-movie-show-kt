package com.taufik.themovieshow.ui.tvshow.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.taufik.themovieshow.data.NetworkResult
import com.taufik.themovieshow.data.repository.TheMovieShowRepository
import com.taufik.themovieshow.model.response.tvshow.discover.DiscoverTvShowsResponse
import com.taufik.themovieshow.model.response.tvshow.popularairingtoday.TvShowsMainResponse
import com.taufik.themovieshow.model.response.tvshow.trending.TvShowsTrendingResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class TvShowsViewModel @Inject constructor(private val repository: TheMovieShowRepository) : ViewModel() {

    fun getDiscoverTvShows(query: String): StateFlow<NetworkResult<DiscoverTvShowsResponse>> =
        repository.getDiscoverTvShows(query).stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(DELAY_EMIT),
            initialValue = NetworkResult.Loading()
        )

    fun getTvShowsAiringToday() : StateFlow<NetworkResult<TvShowsMainResponse>> =
        repository.getTvShowsAiringToday().stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(DELAY_EMIT),
            initialValue = NetworkResult.Loading()
        )

    fun getTvShowsPopular(): StateFlow<NetworkResult<TvShowsMainResponse>> =
        repository.getTvShowsPopular().stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(DELAY_EMIT),
            initialValue = NetworkResult.Loading()
        )

    fun getTvShowsTrending(): StateFlow<NetworkResult<TvShowsTrendingResponse>> =
        repository.getTvShowsTrending().stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(DELAY_EMIT),
            initialValue = NetworkResult.Loading()
        )

    companion object {
        const val DELAY_EMIT = 5000L
    }
}