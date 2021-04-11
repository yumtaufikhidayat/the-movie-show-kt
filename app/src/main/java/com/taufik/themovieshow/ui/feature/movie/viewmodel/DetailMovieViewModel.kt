package com.taufik.themovieshow.ui.feature.movie.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.taufik.themovieshow.api.ApiClient
import com.taufik.themovieshow.ui.feature.movie.data.detail.MovieDetailResponse
import com.taufik.themovieshow.ui.feature.movie.data.video.MovieVideoResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailMovieViewModel : ViewModel() {

    val listDetailMovies = MutableLiveData<MovieDetailResponse>()
    val listDetailVideo = MutableLiveData<MovieVideoResponse>()

    fun setDetailMovieNowPlaying(id: Int, apiKey: String) {
        ApiClient.apiInstance
            .getDetailMovie(id, apiKey)
            .enqueue(object : Callback<MovieDetailResponse> {
                override fun onResponse(
                    call: Call<MovieDetailResponse>,
                    response: Response<MovieDetailResponse>
                ) {
                    if (response.isSuccessful) {
                        listDetailMovies.postValue(response.body())
                        Log.e("listDetailMovies", "onResponse: ${response.body()}" )
                    }
                }

                override fun onFailure(call: Call<MovieDetailResponse>, t: Throwable) {
                    Log.e("errorRetrofit", "onFailure: ${t.localizedMessage}")
                }

            })
    }

    fun getDetailMovieNowPlaying(): LiveData<MovieDetailResponse> {
        return listDetailMovies
    }

    fun setDetailMovieVideo(id: Int, apiKey: String) {
        ApiClient.apiInstance
            .getMovieVideo(id, apiKey)
            .enqueue(object : Callback<MovieVideoResponse> {
                override fun onResponse(
                    call: Call<MovieVideoResponse>,
                    response: Response<MovieVideoResponse>
                ) {
                    if (response.isSuccessful) {
                        listDetailVideo.postValue(response.body())
                        Log.e("listDetailVideo", "onResponse: ${response.body()}")
                    }
                }

                override fun onFailure(call: Call<MovieVideoResponse>, t: Throwable) {
                    Log.e("errorRetrofit", "onFailure: ${t.localizedMessage}")
                }
            })
    }

    fun getDetailMovieVideo(): LiveData<MovieVideoResponse>{
        return listDetailVideo
    }
}