package com.taufik.themovieshow.ui.main.tvshow.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.taufik.themovieshow.data.NetworkResult
import com.taufik.themovieshow.data.paging.tvshow.TvShowsAiringTodayPagingSource
import com.taufik.themovieshow.data.paging.tvshow.TvShowsPopularPagingSource
import com.taufik.themovieshow.data.paging.tvshow.TvShowsTrendingPagingSource
import com.taufik.themovieshow.data.repository.TheMovieShowRepository
import com.taufik.themovieshow.model.response.tvshow.discover.DiscoverTvShowsResponse
import com.taufik.themovieshow.model.response.tvshow.popularairingtoday.TvShowsMainResponse
import com.taufik.themovieshow.model.response.tvshow.popularairingtoday.TvShowsMainResult
import com.taufik.themovieshow.model.response.tvshow.trending.TvShowsTrendingResponse
import com.taufik.themovieshow.model.response.tvshow.trending.TvShowsTrendingResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TvShowsViewModel @Inject constructor(private val repository: TheMovieShowRepository) : ViewModel() {

   /* private val _tvShowAiringTodayResponse: MutableLiveData<NetworkResult<TvShowsMainResponse>> = MutableLiveData()
    val tvShowAiringTodayResponse: LiveData<NetworkResult<TvShowsMainResponse>> = _tvShowAiringTodayResponse
    
    private val _tvShowPopularResponse: MutableLiveData<NetworkResult<TvShowsMainResponse>> = MutableLiveData()
    val tvShowPopularResponse: LiveData<NetworkResult<TvShowsMainResponse>> = _tvShowPopularResponse
    
    private val _tvShowTrendingResponse: MutableLiveData<NetworkResult<TvShowsTrendingResponse>> = MutableLiveData()
    val tvShowTrendingResponse: LiveData<NetworkResult<TvShowsTrendingResponse>> = _tvShowTrendingResponse*/
    
    private val _discoverTvShowsResponse: MutableLiveData<NetworkResult<DiscoverTvShowsResponse>> = MutableLiveData()
    val discoverTvShowsResponse: LiveData<NetworkResult<DiscoverTvShowsResponse>> = _discoverTvShowsResponse
    
    fun setTvShowsAiringToday(): Flow<PagingData<TvShowsMainResult>> = Pager(PagingConfig(1)) {
        TvShowsAiringTodayPagingSource(repository)
    }.flow.cachedIn(viewModelScope)

    fun setTvShowsPopular(): Flow<PagingData<TvShowsMainResult>> = Pager(PagingConfig(1)) {
        TvShowsPopularPagingSource(repository)
    }.flow.cachedIn(viewModelScope)

    fun setTvShowsTrending(): Flow<PagingData<TvShowsTrendingResult>> = Pager(PagingConfig(1)) {
        TvShowsTrendingPagingSource(repository)
    }.flow.cachedIn(viewModelScope)

    fun setDiscoverTvShows(query: String) = viewModelScope.launch {
        repository.getDiscoverTvShows(query).collect {
            _discoverTvShowsResponse.value = it
        }
    }
}