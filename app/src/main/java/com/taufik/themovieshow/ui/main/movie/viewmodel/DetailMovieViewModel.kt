package com.taufik.themovieshow.ui.main.movie.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.taufik.themovieshow.api.ApiClient
import com.taufik.themovieshow.ui.main.movie.data.moviedetail.MovieDetailResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailMovieViewModel : ViewModel() {

    val listDetailMoviesNowPlaying = MutableLiveData<MovieDetailResponse>()

    fun setDetailMovieNowPlaying(id: Int, apiKey: String) {
        ApiClient.apiInstance
            .getDetailMovieNowPlaying(id, apiKey)
            .enqueue(object : Callback<MovieDetailResponse> {
                override fun onResponse(
                    call: Call<MovieDetailResponse>,
                    response: Response<MovieDetailResponse>
                ) {
                    if (response.isSuccessful) {
                        listDetailMoviesNowPlaying.postValue(response.body())
                        Log.e("listUsers", "onResponse: ${response.body()}" )
                    }
                }

                override fun onFailure(call: Call<MovieDetailResponse>, t: Throwable) {
                    Log.e("errorRetrofit", "onFailure: ${t.localizedMessage}")
                }

            })
    }

    fun getDetailMovieNowPlaying(): LiveData<MovieDetailResponse> {
        return listDetailMoviesNowPlaying
    }
}