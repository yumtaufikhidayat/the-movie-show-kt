package com.taufik.themovieshow.data.viewmodel.movie

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.taufik.themovieshow.BuildConfig
import com.taufik.themovieshow.data.api.ApiClient
import com.taufik.themovieshow.data.local.dao.FavoriteMovieDao
import com.taufik.themovieshow.data.local.entity.FavoriteMovie
import com.taufik.themovieshow.data.local.room.MovieDatabase
import com.taufik.themovieshow.data.main.movie.cast.MovieCast
import com.taufik.themovieshow.data.main.movie.cast.MovieCastResponse
import com.taufik.themovieshow.data.main.movie.detail.MovieDetailResponse
import com.taufik.themovieshow.data.main.movie.video.MovieVideoResponse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailMovieViewModel(application: Application) : AndroidViewModel(application) {

    private val apiKey = BuildConfig.API_KEY
    private val apiClient = ApiClient.apiInstance

    private val _listDetailMovies = MutableLiveData<MovieDetailResponse>()
    val detailMovies: LiveData<MovieDetailResponse> = _listDetailMovies

    private val _detailVideo = MutableLiveData<MovieVideoResponse>()
    val detailVideo: LiveData<MovieVideoResponse> = _detailVideo

    private val _listDetailCast = MutableLiveData<ArrayList<MovieCast>>()
    val listDetailCast: LiveData<ArrayList<MovieCast>> = _listDetailCast

    private var movieDao: FavoriteMovieDao?
    private var movieDb: MovieDatabase? = MovieDatabase.getDatabase(context = application)

    init {
        movieDao = movieDb?.favoriteMovieDao()
    }

    fun setDetailMovies(id: Int) {
        apiClient.getDetailMovie(id, apiKey)
            .enqueue(object : Callback<MovieDetailResponse> {
                override fun onResponse(
                    call: Call<MovieDetailResponse>,
                    response: Response<MovieDetailResponse>
                ) {
                    if (response.isSuccessful) {
                        _listDetailMovies.value = response.body()
                        Log.e("listDetailMovies", "onResponse: ${response.body()}" )
                    }
                }

                override fun onFailure(call: Call<MovieDetailResponse>, t: Throwable) {
                    Log.e("errorRetrofit", "onFailure: ${t.localizedMessage}")
                }
            })
    }

    fun setDetailMovieVideo(id: Int) {
        apiClient.getMovieVideo(id, apiKey)
            .enqueue(object : Callback<MovieVideoResponse> {
                override fun onResponse(
                    call: Call<MovieVideoResponse>,
                    response: Response<MovieVideoResponse>
                ) {
                    if (response.isSuccessful) {
                        _detailVideo.value = response.body()
                        Log.e("detailVideo", "onResponse: ${response.body()}")
                    }
                }

                override fun onFailure(call: Call<MovieVideoResponse>, t: Throwable) {
                    Log.e("errorRetrofit", "onFailure: ${t.localizedMessage}")
                }
            })
    }

    fun setDetailMovieCast(id: Int) {
        ApiClient.apiInstance
            .getMovieCast(id, apiKey)
            .enqueue(object : Callback<MovieCastResponse> {
                override fun onResponse(
                    call: Call<MovieCastResponse>,
                    response: Response<MovieCastResponse>
                ) {
                    if (response.isSuccessful) {
                        _listDetailCast.value = response.body()?.cast as ArrayList<MovieCast>
                        Log.e("listDetailCast", "onResponse: ${response.body()}")
                    }
                }

                override fun onFailure(call: Call<MovieCastResponse>, t: Throwable) {
                    Log.e("errorRetrofit", "onFailure: ${t.localizedMessage}")
                }
            })
    }

    // Favorite
    fun addToFavorite(
        movieId: Int,
        posterPath: String,
        title: String,
        releaseDate: String,
        rating: Double
    ) {
        CoroutineScope(Dispatchers.IO).launch {
            val movie = FavoriteMovie(
                movieId, posterPath, title, releaseDate, rating
            )
            movieDao?.addToFavorite(movie)
        }
    }

    suspend fun checkFavorite(movieId: Int) = movieDao?.checkFavorite(movieId)

    fun removeFromFavorite(movieId: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            movieDao?.removeFromFavorite(movieId)
        }
    }
}