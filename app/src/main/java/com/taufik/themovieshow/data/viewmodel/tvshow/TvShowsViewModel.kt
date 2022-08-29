package com.taufik.themovieshow.data.viewmodel.tvshow

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.taufik.themovieshow.api.ApiClient
import com.taufik.themovieshow.data.main.tvshow.discover.DiscoverTvShowsResponse
import com.taufik.themovieshow.data.main.tvshow.discover.DiscoverTvShowsResult
import com.taufik.themovieshow.data.main.tvshow.popularairingtoday.TvShowsMainResponse
import com.taufik.themovieshow.data.main.tvshow.popularairingtoday.TvShowsMainResult
import com.taufik.themovieshow.data.main.tvshow.trending.TvShowsTrendingReponse
import com.taufik.themovieshow.data.main.tvshow.trending.TvShowsTrendingResult
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TvShowsViewModel : ViewModel() {

    private val listAiringToday = MutableLiveData<ArrayList<TvShowsMainResult>>()
    private val listPopular = MutableLiveData<ArrayList<TvShowsMainResult>>()
    private val listTrending = MutableLiveData<ArrayList<TvShowsTrendingResult>>()
    private val listDiscover = MutableLiveData<ArrayList<DiscoverTvShowsResult>>()

    fun setTvShowsAiringToday(apiKey: String) {
        ApiClient.apiInstance
                .getTvShowsAiringToday(apiKey)
                .enqueue(object : Callback<TvShowsMainResponse> {
                    override fun onResponse(call: Call<TvShowsMainResponse>, response: Response<TvShowsMainResponse>) {
                        if (response.isSuccessful) {
                            listAiringToday.postValue(response.body()?.results as ArrayList<TvShowsMainResult>)
                            Log.e("mainSuccess", "onResponse: ${response.body()}")
                        }
                    }

                    override fun onFailure(call: Call<TvShowsMainResponse>, t: Throwable) {
                        Log.e("mainFailed", "onFailure: ${t.localizedMessage}")
                    }
                })
    }

    fun getTvShowsAiringToday(): LiveData<ArrayList<TvShowsMainResult>> {
        return listAiringToday
    }

    fun setTvShowsPopular(apiKey: String) {
        ApiClient.apiInstance
                .getTvShowsPopular(apiKey)
                .enqueue(object : Callback<TvShowsMainResponse> {
                    override fun onResponse(call: Call<TvShowsMainResponse>, response: Response<TvShowsMainResponse>) {
                        if (response.isSuccessful) {
                            listPopular.postValue(response.body()?.results as ArrayList<TvShowsMainResult>)
                            Log.e("mainSuccess", "onResponse: ${response.body()}")
                        }
                    }

                    override fun onFailure(call: Call<TvShowsMainResponse>, t: Throwable) {
                        Log.e("mainFailed", "onFailure: ${t.localizedMessage}")
                    }
                })
    }

    fun getTvShowsPopular(): LiveData<ArrayList<TvShowsMainResult>> {
        return listPopular
    }

    fun setTvShowsTrending(apiKey: String) {
        ApiClient.apiInstance
            .getTvShowsTrending(apiKey)
            .enqueue(object : Callback<TvShowsTrendingReponse> {
                override fun onResponse(
                    call: Call<TvShowsTrendingReponse>,
                    response: Response<TvShowsTrendingReponse>
                ) {
                    if (response.body() != null) {
                        listTrending.postValue(response.body()?.results as ArrayList<TvShowsTrendingResult>)
                        Log.e("mainSuccess", "onResponse: ${response.body()}")
                    }
                }

                override fun onFailure(call: Call<TvShowsTrendingReponse>, t: Throwable) {
                    Log.e("mainFailed", "onFailure: ${t.localizedMessage}")
                }

            })
    }

    fun getTvShowsTrending(): LiveData<ArrayList<TvShowsTrendingResult>> {
        return listTrending
    }

    fun setDiscoverTvShows(apiKey: String, query: String) {
        ApiClient.apiInstance
            .getDiscoverTvShows(apiKey, query)
            .enqueue(object : Callback<DiscoverTvShowsResponse> {
                override fun onResponse(
                    call: Call<DiscoverTvShowsResponse>,
                    response: Response<DiscoverTvShowsResponse>
                ) {
                    if (response.isSuccessful) {
                        listDiscover.postValue(response.body()?.results as ArrayList<DiscoverTvShowsResult>)
                        Log.e("mainSuccess", "onResponse: ${response.body()}")
                    }
                }

                override fun onFailure(call: Call<DiscoverTvShowsResponse>, t: Throwable) {
                    Log.e("mainFailed", "onFailure: ${t.localizedMessage}")
                }
            })
    }

    fun getDiscoverTvShows(): LiveData<ArrayList<DiscoverTvShowsResult>>{
        return listDiscover
    }
}