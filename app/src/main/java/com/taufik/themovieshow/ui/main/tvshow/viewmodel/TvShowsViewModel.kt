package com.taufik.themovieshow.ui.main.tvshow.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.taufik.themovieshow.BuildConfig
import com.taufik.themovieshow.data.remote.api.ApiClient
import com.taufik.themovieshow.model.response.tvshow.discover.DiscoverTvShowsResponse
import com.taufik.themovieshow.model.response.tvshow.discover.DiscoverTvShowsResult
import com.taufik.themovieshow.model.response.tvshow.popularairingtoday.TvShowsMainResponse
import com.taufik.themovieshow.model.response.tvshow.popularairingtoday.TvShowsMainResult
import com.taufik.themovieshow.model.response.tvshow.trending.TvShowsTrendingResponse
import com.taufik.themovieshow.model.response.tvshow.trending.TvShowsTrendingResult
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TvShowsViewModel : ViewModel() {

    private val apiKey = BuildConfig.API_KEY
    private val apiInstance = ApiClient.apiInstance

    private val _listAiringToday = MutableLiveData<ArrayList<TvShowsMainResult>>()
    val listAiringToday: LiveData<ArrayList<TvShowsMainResult>> = _listAiringToday

    private val _listPopular = MutableLiveData<ArrayList<TvShowsMainResult>>()
    val listPopular: LiveData<ArrayList<TvShowsMainResult>> = _listPopular

    private val _listTrending = MutableLiveData<ArrayList<TvShowsTrendingResult>>()
    val listTrending: LiveData<ArrayList<TvShowsTrendingResult>> = _listTrending

    private val _listDiscover = MutableLiveData<ArrayList<DiscoverTvShowsResult>>()
    val listDiscover: LiveData<ArrayList<DiscoverTvShowsResult>> = _listDiscover

    fun setTvShowsAiringToday(apiKey: String) {
        apiInstance.getTvShowsAiringToday(apiKey)
            .enqueue(object :
                Callback<TvShowsMainResponse> {
                override fun onResponse(
                    call: Call<TvShowsMainResponse>,
                    response: Response<TvShowsMainResponse>
                ) {
                    if (response.isSuccessful) {
                        _listAiringToday.value =
                            response.body()?.results as ArrayList<TvShowsMainResult>
                    }
                }

                override fun onFailure(
                    call: Call<TvShowsMainResponse>,
                    t: Throwable
                ) {
                }
            })
    }

    fun setTvShowsPopular() {
        apiInstance.getTvShowsPopular(apiKey)
            .enqueue(object :
                Callback<TvShowsMainResponse> {
                override fun onResponse(
                    call: Call<TvShowsMainResponse>,
                    response: Response<TvShowsMainResponse>
                ) {
                    if (response.isSuccessful) {
                        _listPopular.value =
                            response.body()?.results as ArrayList<TvShowsMainResult>
                    }
                }

                override fun onFailure(
                    call: Call<TvShowsMainResponse>,
                    t: Throwable
                ) {
                }
            })
    }

    fun setTvShowsTrending() {
        apiInstance.getTvShowsTrending(apiKey)
            .enqueue(object :
                Callback<TvShowsTrendingResponse> {
                override fun onResponse(
                    call: Call<TvShowsTrendingResponse>,
                    response: Response<TvShowsTrendingResponse>
                ) {
                    if (response.body() != null) {
                        _listTrending.value =
                            response.body()?.results as ArrayList<TvShowsTrendingResult>
                    }
                }

                override fun onFailure(
                    call: Call<TvShowsTrendingResponse>,
                    t: Throwable
                ) {
                }
            })
    }

    fun setDiscoverTvShows(query: String) {
        apiInstance.getDiscoverTvShows(apiKey, query)
            .enqueue(object :
                Callback<DiscoverTvShowsResponse> {
                override fun onResponse(
                    call: Call<DiscoverTvShowsResponse>,
                    response: Response<DiscoverTvShowsResponse>
                ) {
                    if (response.isSuccessful) {
                        _listDiscover.value =
                            response.body()?.results as ArrayList<DiscoverTvShowsResult>
                    }
                }

                override fun onFailure(
                    call: Call<DiscoverTvShowsResponse>,
                    t: Throwable
                ) {
                }
            })
    }
}