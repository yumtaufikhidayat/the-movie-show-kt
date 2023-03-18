package com.taufik.themovieshow.data.viewmodel.movie

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.taufik.themovieshow.BuildConfig
import com.taufik.themovieshow.data.remote.api.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MovieViewModel : ViewModel() {

    private val apiKey = BuildConfig.API_KEY
    private val apiInstance = ApiClient.apiInstance

    private val _listNowPlaying =
        MutableLiveData<ArrayList<com.taufik.themovieshow.model.response.movie.nowplayingupcoming.MovieMainResult>>()
    val listNowPlaying: LiveData<ArrayList<com.taufik.themovieshow.model.response.movie.nowplayingupcoming.MovieMainResult>> =
        _listNowPlaying

    private val _listUpcoming =
        MutableLiveData<ArrayList<com.taufik.themovieshow.model.response.movie.nowplayingupcoming.MovieMainResult>>()
    val listUpcoming: LiveData<ArrayList<com.taufik.themovieshow.model.response.movie.nowplayingupcoming.MovieMainResult>> =
        _listUpcoming

    private val _listTrendingDay =
        MutableLiveData<ArrayList<com.taufik.themovieshow.model.response.movie.trending.MovieTrendingResult>>()
    val listTrendingDay: LiveData<ArrayList<com.taufik.themovieshow.model.response.movie.trending.MovieTrendingResult>> =
        _listTrendingDay

    private val _listDiscover =
        MutableLiveData<ArrayList<com.taufik.themovieshow.model.response.movie.discover.DiscoverMovieResult>>()
    val listDiscover: LiveData<ArrayList<com.taufik.themovieshow.model.response.movie.discover.DiscoverMovieResult>> =
        _listDiscover

    fun setMovieNowPlaying() {
        apiInstance.getMovieNowPlaying(apiKey)
            .enqueue(object :
                Callback<com.taufik.themovieshow.model.response.movie.nowplayingupcoming.MovieMainResponse> {
                override fun onResponse(
                    call: Call<com.taufik.themovieshow.model.response.movie.nowplayingupcoming.MovieMainResponse>,
                    response: Response<com.taufik.themovieshow.model.response.movie.nowplayingupcoming.MovieMainResponse>
                ) {
                    if (response.isSuccessful) {
                        _listNowPlaying.value =
                            response.body()?.results as ArrayList<com.taufik.themovieshow.model.response.movie.nowplayingupcoming.MovieMainResult>
                    }
                }

                override fun onFailure(
                    call: Call<com.taufik.themovieshow.model.response.movie.nowplayingupcoming.MovieMainResponse>,
                    t: Throwable
                ) {
                }
            })
    }

    fun setMovieUpcoming() {
        apiInstance.getMovieUpcoming(apiKey)
            .enqueue(object :
                Callback<com.taufik.themovieshow.model.response.movie.nowplayingupcoming.MovieMainResponse> {
                override fun onResponse(
                    call: Call<com.taufik.themovieshow.model.response.movie.nowplayingupcoming.MovieMainResponse>,
                    response: Response<com.taufik.themovieshow.model.response.movie.nowplayingupcoming.MovieMainResponse>
                ) {
                    if (response.isSuccessful) {
                        _listUpcoming.value =
                            response.body()?.results as ArrayList<com.taufik.themovieshow.model.response.movie.nowplayingupcoming.MovieMainResult>
                    }
                }

                override fun onFailure(
                    call: Call<com.taufik.themovieshow.model.response.movie.nowplayingupcoming.MovieMainResponse>,
                    t: Throwable
                ) {
                }
            })
    }

    fun setMovieTrendingDay() {
        apiInstance.getMovieTrendingDay(apiKey)
            .enqueue(object :
                Callback<com.taufik.themovieshow.model.response.movie.trending.MovieTrendingResponse> {
                override fun onResponse(
                    call: Call<com.taufik.themovieshow.model.response.movie.trending.MovieTrendingResponse>,
                    response: Response<com.taufik.themovieshow.model.response.movie.trending.MovieTrendingResponse>
                ) {
                    if (response.isSuccessful) {
                        _listTrendingDay.value =
                            response.body()?.results as ArrayList<com.taufik.themovieshow.model.response.movie.trending.MovieTrendingResult>
                    }
                }

                override fun onFailure(
                    call: Call<com.taufik.themovieshow.model.response.movie.trending.MovieTrendingResponse>,
                    t: Throwable
                ) {
                }
            })
    }

    fun setDiscoverMovie(query: String) {
        apiInstance.getDiscoverMovie(apiKey, query)
            .enqueue(object :
                Callback<com.taufik.themovieshow.model.response.movie.discover.DiscoverMovieResponse> {
                override fun onResponse(
                    call: Call<com.taufik.themovieshow.model.response.movie.discover.DiscoverMovieResponse>,
                    response: Response<com.taufik.themovieshow.model.response.movie.discover.DiscoverMovieResponse>
                ) {
                    if (response.isSuccessful) {
                        _listDiscover.value =
                            response.body()?.results as ArrayList<com.taufik.themovieshow.model.response.movie.discover.DiscoverMovieResult>
                    }
                }

                override fun onFailure(
                    call: Call<com.taufik.themovieshow.model.response.movie.discover.DiscoverMovieResponse>,
                    t: Throwable
                ) {
                }
            })
    }
}