package com.taufik.themovieshow.ui.main.tvshow.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.taufik.themovieshow.api.ApiClient
import com.taufik.themovieshow.ui.main.tvshow.data.popular.TvShowPopularResponse
import com.taufik.themovieshow.ui.main.tvshow.data.popular.TvShowPopularResult
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TvShowViewModel : ViewModel() {

    private val listTvShowsPopular = MutableLiveData<ArrayList<TvShowPopularResult>>()
    private val listTvShowsAiringToday = MutableLiveData<ArrayList<TvShowPopularResult>>()

    fun setTvShowsPopular(apiKey: String) {
        ApiClient.apiInstance
                .getTvShowsPopular(apiKey)
                .enqueue(object : Callback<TvShowPopularResponse> {
                    override fun onResponse(call: Call<TvShowPopularResponse>, response: Response<TvShowPopularResponse>) {
                        if (response.isSuccessful) {
                            listTvShowsPopular.postValue(response.body()?.results as ArrayList<TvShowPopularResult>)
                            Log.e("listTvShowsPopular", "onResponse: ${response.body()}")
                        }
                    }

                    override fun onFailure(call: Call<TvShowPopularResponse>, t: Throwable) {
                        Log.e("errorRetrofit", "onFailure: ${t.localizedMessage}")
                    }
                })
    }

    fun getTvShowsPopular(): LiveData<ArrayList<TvShowPopularResult>> {
        return listTvShowsPopular
    }

    fun setTvShowsAiringToday(apiKey: String) {
        ApiClient.apiInstance
            .getTvShowsAiringToday(apiKey)
            .enqueue(object : Callback<TvShowPopularResponse> {
                override fun onResponse(call: Call<TvShowPopularResponse>, response: Response<TvShowPopularResponse>) {
                    if (response.isSuccessful) {
                        listTvShowsAiringToday.postValue(response.body()?.results as ArrayList<TvShowPopularResult>)
                        Log.e("listTvShowsPopular", "onResponse: ${response.body()}")
                    }
                }

                override fun onFailure(call: Call<TvShowPopularResponse>, t: Throwable) {
                    Log.e("errorRetrofit", "onFailure: ${t.localizedMessage}")
                }
            })
    }

    fun getTvShowsAiringToday(): LiveData<ArrayList<TvShowPopularResult>> {
        return listTvShowsAiringToday
    }
}