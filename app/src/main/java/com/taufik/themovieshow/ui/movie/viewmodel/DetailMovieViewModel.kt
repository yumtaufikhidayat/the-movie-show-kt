package com.taufik.themovieshow.ui.movie.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.taufik.themovieshow.api.ApiClient
import com.taufik.themovieshow.ui.favorite.data.movie.FavoriteMovie
import com.taufik.themovieshow.ui.favorite.data.movie.FavoriteMovieDao
import com.taufik.themovieshow.ui.favorite.data.movie.MovieDatabase
import com.taufik.themovieshow.ui.movie.model.cast.MovieCast
import com.taufik.themovieshow.ui.movie.model.cast.MovieCastResponse
import com.taufik.themovieshow.ui.movie.model.detail.MovieDetailResponse
import com.taufik.themovieshow.ui.movie.model.video.MovieVideoResponse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailMovieViewModel(application: Application) : AndroidViewModel(application) {

    val listDetailMovies = MutableLiveData<MovieDetailResponse>()
    val listDetailVideo = MutableLiveData<MovieVideoResponse>()
    val listDetailCast = MutableLiveData<ArrayList<MovieCast>>()

    private var movieDao: FavoriteMovieDao?
    private var movieDb: MovieDatabase? = MovieDatabase.getDatabase(context = application)

    init {
        movieDao = movieDb?.favoriteMovieDao()
    }

    fun setDetailMovies(id: Int, apiKey: String) {
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

    fun getDetailMovies(): LiveData<MovieDetailResponse> {
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

    fun setDetailMovieCast(id: Int, apiKey: String) {
        ApiClient.apiInstance
            .getMovieCast(id, apiKey)
            .enqueue(object : Callback<MovieCastResponse> {
                override fun onResponse(
                    call: Call<MovieCastResponse>,
                    response: Response<MovieCastResponse>
                ) {
                    if (response.isSuccessful) {
                        listDetailCast.postValue(response.body()?.cast as ArrayList<MovieCast>)
                        Log.e("listDetailCast", "onResponse: ${response.body()}")
                    }
                }

                override fun onFailure(call: Call<MovieCastResponse>, t: Throwable) {
                    Log.e("errorRetrofit", "onFailure: ${t.localizedMessage}")
                }
            })
    }

    fun getDetailMovieCast(): LiveData<ArrayList<MovieCast>> {
        return listDetailCast
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