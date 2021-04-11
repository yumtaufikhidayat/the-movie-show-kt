package com.taufik.themovieshow.ui.feature.tvshow.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.taufik.themovieshow.api.ApiClient
import com.taufik.themovieshow.ui.feature.tvshow.data.cast.TvShowsCast
import com.taufik.themovieshow.ui.feature.tvshow.data.cast.TvShowsCastResponse
import com.taufik.themovieshow.ui.feature.tvshow.data.detail.TvShowsPopularDetailResponse
import com.taufik.themovieshow.ui.feature.tvshow.data.video.TvShowsVideoResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailTvShowViewModel : ViewModel() {

    val listDetailTvShows = MutableLiveData<TvShowsPopularDetailResponse>()
    val listDetailVideo = MutableLiveData<TvShowsVideoResponse>()
    val listDetailCast = MutableLiveData<ArrayList<TvShowsCast>>()

    fun setDetailTvShowPopular(id: Int, apiKey: String) {
        ApiClient.apiInstance
                .getDetailTvShows(id, apiKey)
                .enqueue(object : Callback<TvShowsPopularDetailResponse> {
                    override fun onResponse(call: Call<TvShowsPopularDetailResponse>, response: Response<TvShowsPopularDetailResponse>) {
                        if (response.isSuccessful) {
                            listDetailTvShows.postValue(response.body())
                            Log.e("listDetailTvShows", "onResponse: ${response.body()}")
                        }
                    }

                    override fun onFailure(call: Call<TvShowsPopularDetailResponse>, t: Throwable) {
                        Log.e("errorRetrofit", "onFailure: ${t.localizedMessage}")
                    }
                })
    }

    fun getDetailTvShowsPopular(): LiveData<TvShowsPopularDetailResponse> {
        return listDetailTvShows
    }

    fun setDetailTvShowVideo(id: Int, apiKey: String) {
        ApiClient.apiInstance
            .getTvShowsVideo(id, apiKey)
            .enqueue(object : Callback<TvShowsVideoResponse> {
                override fun onResponse(
                    call: Call<TvShowsVideoResponse>,
                    response: Response<TvShowsVideoResponse>
                ) {
                    if (response.isSuccessful) {
                        listDetailVideo.postValue(response.body())
                        Log.e("listDetailVideo", "onResponse: ${response.body()}")
                    }
                }

                override fun onFailure(call: Call<TvShowsVideoResponse>, t: Throwable) {
                    Log.e("errorRetrofit", "onFailure: ${t.localizedMessage}")
                }
            })
    }

    fun getDetailTvShowsVideo(): LiveData<TvShowsVideoResponse> {
        return listDetailVideo
    }

    fun setDetailTvShowsCast(id: Int, apiKey: String) {
        ApiClient.apiInstance
            .getTvShowsCast(id, apiKey)
            .enqueue(object : Callback<TvShowsCastResponse> {
                override fun onResponse(
                    call: Call<TvShowsCastResponse>,
                    response: Response<TvShowsCastResponse>
                ) {
                    if (response.isSuccessful) {
                        listDetailCast.postValue(response.body()?.cast as ArrayList<TvShowsCast>)
                        Log.e("listDetailCast", "onResponse: ${response.body()}")
                    }
                }

                override fun onFailure(call: Call<TvShowsCastResponse>, t: Throwable) {
                    Log.e("errorRetrofit", "onFailure: ${t.localizedMessage}")
                }
            })
    }

    fun getDetailTvShowsCast(): LiveData<ArrayList<TvShowsCast>> {
        return listDetailCast
    }
}