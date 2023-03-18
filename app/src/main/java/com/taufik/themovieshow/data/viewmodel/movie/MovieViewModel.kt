package com.taufik.themovieshow.data.viewmodel.movie

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.taufik.themovieshow.BuildConfig
import com.taufik.themovieshow.data.remote.api.ApiClient
import com.taufik.themovieshow.data.main.movie.discover.DiscoverMovieResponse
import com.taufik.themovieshow.data.main.movie.discover.DiscoverMovieResult
import com.taufik.themovieshow.data.main.movie.nowplayingupcoming.MovieMainResponse
import com.taufik.themovieshow.data.main.movie.nowplayingupcoming.MovieMainResult
import com.taufik.themovieshow.data.main.movie.trending.MovieTrendingResponse
import com.taufik.themovieshow.data.main.movie.trending.MovieTrendingResult
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MovieViewModel : ViewModel() {

    private val apiKey = BuildConfig.API_KEY
    private val apiInstance = ApiClient.apiInstance

    private val _listNowPlaying = MutableLiveData<ArrayList<MovieMainResult>>()
    val listNowPlaying: LiveData<ArrayList<MovieMainResult>> = _listNowPlaying

    private val _listUpcoming = MutableLiveData<ArrayList<MovieMainResult>>()
    val listUpcoming: LiveData<ArrayList<MovieMainResult>> = _listUpcoming

    private val _listTrendingDay  = MutableLiveData<ArrayList<MovieTrendingResult>>()
    val listTrendingDay: LiveData<ArrayList<MovieTrendingResult>> = _listTrendingDay

    private val _listDiscover = MutableLiveData<ArrayList<DiscoverMovieResult>>()
    val listDiscover: LiveData<ArrayList<DiscoverMovieResult>> = _listDiscover

    fun setMovieNowPlaying(){
        apiInstance.getMovieNowPlaying(apiKey)
            .enqueue(object : Callback<MovieMainResponse> {
                override fun onResponse(
                    call: Call<MovieMainResponse>,
                    response: Response<MovieMainResponse>
                ) {
                    if (response.isSuccessful) {
                        _listNowPlaying.value = response.body()?.results as ArrayList<MovieMainResult>
                    }
                }

                override fun onFailure(call: Call<MovieMainResponse>, t: Throwable) {}
            })
    }

    fun setMovieUpcoming(){
        apiInstance.getMovieUpcoming(apiKey)
            .enqueue(object : Callback<MovieMainResponse> {
                override fun onResponse(
                    call: Call<MovieMainResponse>,
                    response: Response<MovieMainResponse>
                ) {
                    if (response.isSuccessful) {
                        _listUpcoming.value = response.body()?.results as ArrayList<MovieMainResult>
                    }
                }

                override fun onFailure(call: Call<MovieMainResponse>, t: Throwable) {}
            })
    }

    fun setMovieTrendingDay(){
        apiInstance.getMovieTrendingDay(apiKey)
            .enqueue(object : Callback<MovieTrendingResponse> {
                override fun onResponse(
                    call: Call<MovieTrendingResponse>,
                    response: Response<MovieTrendingResponse>
                ) {
                    if (response.isSuccessful) {
                        _listTrendingDay.value = response.body()?.results as ArrayList<MovieTrendingResult>
                    }
                }

                override fun onFailure(call: Call<MovieTrendingResponse>, t: Throwable) {}
            })
    }

    fun setDiscoverMovie(query: String){
        apiInstance.getDiscoverMovie(apiKey, query)
            .enqueue(object : Callback<DiscoverMovieResponse> {
                override fun onResponse(
                    call: Call<DiscoverMovieResponse>,
                    response: Response<DiscoverMovieResponse>
                ) {
                    if (response.isSuccessful) {
                        _listDiscover.value = response.body()?.results as ArrayList<DiscoverMovieResult>
                    }
                }

                override fun onFailure(call: Call<DiscoverMovieResponse>, t: Throwable) {}
            })
    }
}