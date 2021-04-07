package com.taufik.themovieshow.ui.main.movie.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.taufik.themovieshow.api.ApiClient
import com.taufik.themovieshow.ui.main.movie.data.main.MovieResponse
import com.taufik.themovieshow.ui.main.movie.data.main.MovieResult
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MovieViewModel : ViewModel(){

    private val listMoviesNowPlaying = MutableLiveData<ArrayList<MovieResult>>()
    private val listMoviesUpcoming = MutableLiveData<ArrayList<MovieResult>>()

    fun setMovieNowPlaying(apiKey: String) {
        ApiClient.apiInstance
            .getMovieNowPlaying(apiKey)
            .enqueue(object : Callback<MovieResponse> {
                override fun onResponse(
                    call: Call<MovieResponse>,
                    response: Response<MovieResponse>
                ) {
                    if (response.isSuccessful) {
                        listMoviesNowPlaying.postValue(response.body()?.results as ArrayList<MovieResult>?)
                        Log.e("listUsers", "onResponse: ${response.body()}" )
                    }
                }

                override fun onFailure(call: Call<MovieResponse>, t: Throwable) {
                    Log.e("errorRetrofit", "onFailure: ${t.localizedMessage}")
                }
            })
    }

    fun getMovieNowPlaying(): LiveData<ArrayList<MovieResult>> {
        return listMoviesNowPlaying
    }

    fun setMovieUpcoming(apiKey: String) {
        ApiClient.apiInstance
            .getMovieUpcoming(apiKey)
            .enqueue(object : Callback<MovieResponse> {
                override fun onResponse(
                    call: Call<MovieResponse>,
                    response: Response<MovieResponse>
                ) {
                    if (response.isSuccessful) {
                        listMoviesUpcoming.postValue(response.body()?.results as ArrayList<MovieResult>?)
                        Log.e("listUsers", "onResponse: ${response.body()}" )
                    }
                }

                override fun onFailure(call: Call<MovieResponse>, t: Throwable) {
                    Log.e("errorRetrofit", "onFailure: ${t.localizedMessage}")
                }
            })
    }

    fun getMovieUpcoming(): LiveData<ArrayList<MovieResult>> {
        return listMoviesUpcoming
    }
}