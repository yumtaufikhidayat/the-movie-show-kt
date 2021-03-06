package com.taufik.themovieshow.ui.movie.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.taufik.themovieshow.api.ApiClient
import com.taufik.themovieshow.ui.movie.model.discover.DiscoverMovieResponse
import com.taufik.themovieshow.ui.movie.model.discover.DiscoverMovieResult
import com.taufik.themovieshow.ui.movie.model.nowplayingupcoming.MovieMainResponse
import com.taufik.themovieshow.ui.movie.model.nowplayingupcoming.MovieMainResult
import com.taufik.themovieshow.ui.movie.model.trending.MovieTrendingResponse
import com.taufik.themovieshow.ui.movie.model.trending.MovieTrendingResult
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MovieViewModel : ViewModel() {

    private val listNowPlaying = MutableLiveData<ArrayList<MovieMainResult>>()
    private val listUpcoming = MutableLiveData<ArrayList<MovieMainResult>>()
    private val listTrendingDay  = MutableLiveData<ArrayList<MovieTrendingResult>>()
    private val listDiscover = MutableLiveData<ArrayList<DiscoverMovieResult>>()

    fun setMovieNowPlaying(apiKey: String){
        ApiClient.apiInstance
                .getMovieNowPlaying(apiKey)
                .enqueue(object : Callback<MovieMainResponse> {
                    override fun onResponse(
                            call: Call<MovieMainResponse>,
                            response: Response<MovieMainResponse>
                    ) {
                        if (response.isSuccessful) {
                            listNowPlaying.postValue(response.body()?.results as ArrayList<MovieMainResult>)
                            Log.e("mainSuccess", "onResponse: ${response.body()}")
                        }
                    }

                    override fun onFailure(call: Call<MovieMainResponse>, t: Throwable) {
                        Log.e("mainFailed", "onFailure: ${t.localizedMessage}")
                    }
                })
    }

    fun getMovieNowPlaying(): LiveData<ArrayList<MovieMainResult>> {
        return listNowPlaying
    }

    fun setMovieUpcoming(apiKey: String){
        ApiClient.apiInstance
                .getMovieUpcoming(apiKey)
                .enqueue(object : Callback<MovieMainResponse> {
                    override fun onResponse(
                            call: Call<MovieMainResponse>,
                            response: Response<MovieMainResponse>
                    ) {
                        if (response.isSuccessful) {
                            listUpcoming.postValue(response.body()?.results as ArrayList<MovieMainResult>)
                            Log.e("mainSuccess", "onResponse: ${response.body()}")
                        }
                    }

                    override fun onFailure(call: Call<MovieMainResponse>, t: Throwable) {
                        Log.e("mainFailed", "onFailure: ${t.localizedMessage}")
                    }
                })
    }

    fun getMovieUpcoming(): LiveData<ArrayList<MovieMainResult>> {
        return listUpcoming
    }

    fun setMovieTrendingDay(apiKey: String){
        ApiClient.apiInstance
                .getMovieTrendingDay(apiKey)
                .enqueue(object : Callback<MovieTrendingResponse> {
                    override fun onResponse(call: Call<MovieTrendingResponse>, response: Response<MovieTrendingResponse>) {
                        if (response.isSuccessful) {
                            listTrendingDay.postValue(response.body()?.results as ArrayList<MovieTrendingResult>)
                            Log.e("mainSuccess", "onResponse: ${response.body()}")
                        }
                    }

                    override fun onFailure(call: Call<MovieTrendingResponse>, t: Throwable) {
                        Log.e("mainFailed", "onFailure: ${t.localizedMessage}")
                    }
                })
    }

    fun getMovieTrendingDay(): LiveData<ArrayList<MovieTrendingResult>> {
        return listTrendingDay
    }

    fun setDiscoverMovie(apiKey: String, query: String){
        ApiClient.apiInstance
            .getDiscoverMovie(apiKey, query)
            .enqueue(object : Callback<DiscoverMovieResponse> {
                override fun onResponse(
                    call: Call<DiscoverMovieResponse>,
                    response: Response<DiscoverMovieResponse>
                ) {
                    if (response.isSuccessful) {
                        listDiscover.postValue(response.body()?.results as ArrayList<DiscoverMovieResult>)
                        Log.e("mainSuccess", "onResponse: ${response.body()}")
                    }
                }

                override fun onFailure(call: Call<DiscoverMovieResponse>, t: Throwable) {
                    Log.e("mainFailed", "onFailure: ${t.localizedMessage}")
                }
            })
    }

    fun getDiscoverMovie(): LiveData<ArrayList<DiscoverMovieResult>> {
        return listDiscover
    }
}