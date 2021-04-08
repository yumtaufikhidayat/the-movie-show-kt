package com.taufik.themovieshow.ui.main.tvshow.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.taufik.themovieshow.api.ApiClient
import com.taufik.themovieshow.ui.main.tvshow.data.main.TvShowsMainResponse
import com.taufik.themovieshow.ui.main.tvshow.data.main.TvShowsMainResult
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TvShowsViewModel : ViewModel() {

    private val listAiringToday = MutableLiveData<ArrayList<TvShowsMainResult>>()
    private val listPopular = MutableLiveData<ArrayList<TvShowsMainResult>>()

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
}