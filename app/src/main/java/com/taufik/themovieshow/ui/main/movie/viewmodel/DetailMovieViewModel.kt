package com.taufik.themovieshow.ui.main.movie.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.taufik.themovieshow.api.ApiClient
import com.taufik.themovieshow.ui.main.movie.data.nowplayingdetail.MovieNowPlayingDetailResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailMovieViewModel : ViewModel() {

    val listDetailMoviesNowPlaying = MutableLiveData<MovieNowPlayingDetailResponse>()

    fun setDetailMovieNowPlaying(id: Int, apiKey: String) {
        ApiClient.apiInstance
            .getDetailMovieNowPlaying(id, apiKey)
            .enqueue(object : Callback<MovieNowPlayingDetailResponse> {
                override fun onResponse(
                        call: Call<MovieNowPlayingDetailResponse>,
                        response: Response<MovieNowPlayingDetailResponse>
                ) {
                    if (response.isSuccessful) {
                        listDetailMoviesNowPlaying.postValue(response.body())
                        Log.e("listUsers", "onResponse: ${response.body()}" )
                    }
                }

                override fun onFailure(call: Call<MovieNowPlayingDetailResponse>, t: Throwable) {
                    Log.e("errorRetrofit", "onFailure: ${t.localizedMessage}")
                }

            })
    }

    fun getDetailMovieNowPlaying(): LiveData<MovieNowPlayingDetailResponse> {
        return listDetailMoviesNowPlaying
    }
}