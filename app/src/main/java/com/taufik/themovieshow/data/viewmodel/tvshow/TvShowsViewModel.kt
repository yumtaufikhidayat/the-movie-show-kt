package com.taufik.themovieshow.data.viewmodel.tvshow

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.taufik.themovieshow.BuildConfig
import com.taufik.themovieshow.data.remote.api.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TvShowsViewModel : ViewModel() {

    private val apiKey = BuildConfig.API_KEY
    private val apiInstance = ApiClient.apiInstance

    private val _listAiringToday =
        MutableLiveData<ArrayList<com.taufik.themovieshow.model.response.tvshow.popularairingtoday.TvShowsMainResult>>()
    val listAiringToday: LiveData<ArrayList<com.taufik.themovieshow.model.response.tvshow.popularairingtoday.TvShowsMainResult>> =
        _listAiringToday

    private val _listPopular =
        MutableLiveData<ArrayList<com.taufik.themovieshow.model.response.tvshow.popularairingtoday.TvShowsMainResult>>()
    val listPopular: LiveData<ArrayList<com.taufik.themovieshow.model.response.tvshow.popularairingtoday.TvShowsMainResult>> =
        _listPopular

    private val _listTrending =
        MutableLiveData<ArrayList<com.taufik.themovieshow.model.response.tvshow.trending.TvShowsTrendingResult>>()
    val listTrending: LiveData<ArrayList<com.taufik.themovieshow.model.response.tvshow.trending.TvShowsTrendingResult>> =
        _listTrending

    private val _listDiscover =
        MutableLiveData<ArrayList<com.taufik.themovieshow.model.response.tvshow.discover.DiscoverTvShowsResult>>()
    val listDiscover: LiveData<ArrayList<com.taufik.themovieshow.model.response.tvshow.discover.DiscoverTvShowsResult>> =
        _listDiscover

    fun setTvShowsAiringToday(apiKey: String) {
        apiInstance.getTvShowsAiringToday(apiKey)
            .enqueue(object :
                Callback<com.taufik.themovieshow.model.response.tvshow.popularairingtoday.TvShowsMainResponse> {
                override fun onResponse(
                    call: Call<com.taufik.themovieshow.model.response.tvshow.popularairingtoday.TvShowsMainResponse>,
                    response: Response<com.taufik.themovieshow.model.response.tvshow.popularairingtoday.TvShowsMainResponse>
                ) {
                    if (response.isSuccessful) {
                        _listAiringToday.value =
                            response.body()?.results as ArrayList<com.taufik.themovieshow.model.response.tvshow.popularairingtoday.TvShowsMainResult>
                    }
                }

                override fun onFailure(
                    call: Call<com.taufik.themovieshow.model.response.tvshow.popularairingtoday.TvShowsMainResponse>,
                    t: Throwable
                ) {
                }
            })
    }

    fun setTvShowsPopular() {
        apiInstance.getTvShowsPopular(apiKey)
            .enqueue(object :
                Callback<com.taufik.themovieshow.model.response.tvshow.popularairingtoday.TvShowsMainResponse> {
                override fun onResponse(
                    call: Call<com.taufik.themovieshow.model.response.tvshow.popularairingtoday.TvShowsMainResponse>,
                    response: Response<com.taufik.themovieshow.model.response.tvshow.popularairingtoday.TvShowsMainResponse>
                ) {
                    if (response.isSuccessful) {
                        _listPopular.value =
                            response.body()?.results as ArrayList<com.taufik.themovieshow.model.response.tvshow.popularairingtoday.TvShowsMainResult>
                    }
                }

                override fun onFailure(
                    call: Call<com.taufik.themovieshow.model.response.tvshow.popularairingtoday.TvShowsMainResponse>,
                    t: Throwable
                ) {
                }
            })
    }

    fun setTvShowsTrending() {
        apiInstance.getTvShowsTrending(apiKey)
            .enqueue(object :
                Callback<com.taufik.themovieshow.model.response.tvshow.trending.TvShowsTrendingReponse> {
                override fun onResponse(
                    call: Call<com.taufik.themovieshow.model.response.tvshow.trending.TvShowsTrendingReponse>,
                    response: Response<com.taufik.themovieshow.model.response.tvshow.trending.TvShowsTrendingReponse>
                ) {
                    if (response.body() != null) {
                        _listTrending.value =
                            response.body()?.results as ArrayList<com.taufik.themovieshow.model.response.tvshow.trending.TvShowsTrendingResult>
                    }
                }

                override fun onFailure(
                    call: Call<com.taufik.themovieshow.model.response.tvshow.trending.TvShowsTrendingReponse>,
                    t: Throwable
                ) {
                }
            })
    }

    fun setDiscoverTvShows(query: String) {
        apiInstance.getDiscoverTvShows(apiKey, query)
            .enqueue(object :
                Callback<com.taufik.themovieshow.model.response.tvshow.discover.DiscoverTvShowsResponse> {
                override fun onResponse(
                    call: Call<com.taufik.themovieshow.model.response.tvshow.discover.DiscoverTvShowsResponse>,
                    response: Response<com.taufik.themovieshow.model.response.tvshow.discover.DiscoverTvShowsResponse>
                ) {
                    if (response.isSuccessful) {
                        _listDiscover.value =
                            response.body()?.results as ArrayList<com.taufik.themovieshow.model.response.tvshow.discover.DiscoverTvShowsResult>
                    }
                }

                override fun onFailure(
                    call: Call<com.taufik.themovieshow.model.response.tvshow.discover.DiscoverTvShowsResponse>,
                    t: Throwable
                ) {
                }
            })
    }
}