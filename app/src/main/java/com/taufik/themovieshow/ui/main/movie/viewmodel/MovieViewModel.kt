package com.taufik.themovieshow.ui.main.movie.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.taufik.themovieshow.api.ApiClient
import com.taufik.themovieshow.ui.main.movie.data.movie.MovieResponse
import com.taufik.themovieshow.ui.main.movie.data.movie.MovieResult
import com.taufik.themovieshow.ui.main.movie.data.moviedetail.MovieDetailResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MovieViewModel : ViewModel(){

    val listMoviesNowPlaying = MutableLiveData<ArrayList<MovieResult>>()

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
}