package com.taufik.themovieshow.ui.main.tvshow.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.liveData
import com.taufik.themovieshow.data.NetworkResult
import com.taufik.themovieshow.data.paging.tvshow.TvShowsAiringTodayPagingSource
import com.taufik.themovieshow.data.paging.tvshow.TvShowsPopularPagingSource
import com.taufik.themovieshow.data.repository.TheMovieShowRepository
import com.taufik.themovieshow.model.response.tvshow.discover.DiscoverTvShowsResponse
import com.taufik.themovieshow.utils.CommonConstants
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TvShowsViewModel @Inject constructor(private val repository: TheMovieShowRepository) : ViewModel() {

    private val _discoverTvShowsResponse: MutableLiveData<NetworkResult<DiscoverTvShowsResponse>> = MutableLiveData()
    val discoverTvShowsResponse: LiveData<NetworkResult<DiscoverTvShowsResponse>> get() = _discoverTvShowsResponse

    fun setTvShowsAiringToday() = Pager(
        PagingConfig(
            pageSize = CommonConstants.STARTING_PAGE_INDEX,
            maxSize = CommonConstants.LOAD_PER_PAGE,
            enablePlaceholders = false
        ), pagingSourceFactory = {
            TvShowsAiringTodayPagingSource(repository)
        }).liveData

    fun setTvShowsPopular() = Pager(
        PagingConfig(
            pageSize = CommonConstants.STARTING_PAGE_INDEX,
            maxSize = CommonConstants.LOAD_PER_PAGE,
            enablePlaceholders = false
        ), pagingSourceFactory = {
            TvShowsPopularPagingSource(repository)
        }).liveData

    fun setTvShowsTrending() = repository.getTvShowsTrending()

    fun setDiscoverTvShows(query: String) = viewModelScope.launch {
        repository.getDiscoverTvShows(query).collect {
            _discoverTvShowsResponse.value = it
        }
    }
}