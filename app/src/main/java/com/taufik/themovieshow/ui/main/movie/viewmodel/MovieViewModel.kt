package com.taufik.themovieshow.ui.main.movie.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.taufik.themovieshow.api.ApiClient
import com.taufik.themovieshow.ui.main.movie.data.nowplaying.MovieNowPlayingResponse
import com.taufik.themovieshow.ui.main.movie.data.nowplaying.MovieNowPlayingResult
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MovieViewModel : ViewModel(){

    val listMoviesNowPlaying = MutableLiveData<ArrayList<MovieNowPlayingResult>>()

    fun setMovieNowPlaying(apiKey: String) {
        ApiClient.apiInstance
            .getMovieNowPlaying(apiKey)
            .enqueue(object : Callback<MovieNowPlayingResponse> {
                override fun onResponse(
                        call: Call<MovieNowPlayingResponse>,
                        response: Response<MovieNowPlayingResponse>
                ) {
                    if (response.isSuccessful) {
                        listMoviesNowPlaying.postValue(response.body()?.results as ArrayList<MovieNowPlayingResult>?)
                        Log.e("listUsers", "onResponse: ${response.body()}" )
                    }
                }

                override fun onFailure(call: Call<MovieNowPlayingResponse>, t: Throwable) {
                    Log.e("errorRetrofit", "onFailure: ${t.localizedMessage}")
                }
            })
    }

    fun getMovieNowPlaying(): LiveData<ArrayList<MovieNowPlayingResult>> {
        return listMoviesNowPlaying
    }
}