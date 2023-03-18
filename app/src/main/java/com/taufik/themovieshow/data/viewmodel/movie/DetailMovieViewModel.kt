package com.taufik.themovieshow.data.viewmodel.movie

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.taufik.themovieshow.BuildConfig
import com.taufik.themovieshow.data.local.dao.FavoriteMovieDao
import com.taufik.themovieshow.data.local.entity.FavoriteMovie
import com.taufik.themovieshow.data.local.room.MovieDatabase
import com.taufik.themovieshow.data.remote.api.ApiClient
import com.taufik.themovieshow.model.response.movie.detail.MovieDetailResponse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailMovieViewModel(application: Application) : AndroidViewModel(application) {

    private val apiKey = BuildConfig.API_KEY
    private val apiInstance = ApiClient.apiInstance

    private val _listDetailMovies =
        MutableLiveData<MovieDetailResponse>()
    val detailMovies: LiveData<MovieDetailResponse> =
        _listDetailMovies

    private val _detailVideo =
        MutableLiveData<com.taufik.themovieshow.model.response.movie.video.MovieVideoResponse>()
    val detailVideo: LiveData<com.taufik.themovieshow.model.response.movie.video.MovieVideoResponse> =
        _detailVideo

    private val _listDetailCast =
        MutableLiveData<ArrayList<com.taufik.themovieshow.model.response.movie.cast.MovieCast>>()
    val listDetailCast: LiveData<ArrayList<com.taufik.themovieshow.model.response.movie.cast.MovieCast>> =
        _listDetailCast

    private val _listReviews =
        MutableLiveData<ArrayList<com.taufik.themovieshow.model.response.common.reviews.ReviewsResult>>()
    val listReviewMovie: LiveData<ArrayList<com.taufik.themovieshow.model.response.common.reviews.ReviewsResult>> =
        _listReviews

    private val _listSimilar =
        MutableLiveData<ArrayList<com.taufik.themovieshow.model.response.movie.similar.MovieSimilarResult>>()
    val listSimilarMovie: LiveData<ArrayList<com.taufik.themovieshow.model.response.movie.similar.MovieSimilarResult>> =
        _listSimilar

    private var movieDao: FavoriteMovieDao?
    private var movieDb: MovieDatabase? = MovieDatabase.getDatabase(context = application)

    init {
        movieDao = movieDb?.favoriteMovieDao()
    }

    fun setDetailMovies(id: Int) {
        apiInstance.getDetailMovie(id, apiKey)
            .enqueue(object :
                Callback<MovieDetailResponse> {
                override fun onResponse(
                    call: Call<MovieDetailResponse>,
                    response: Response<MovieDetailResponse>
                ) {
                    if (response.isSuccessful) {
                        _listDetailMovies.value = response.body()
                    }
                }

                override fun onFailure(
                    call: Call<MovieDetailResponse>,
                    t: Throwable
                ) {
                }
            })
    }

    fun setDetailMovieCast(id: Int) {
        apiInstance.getMovieCast(id, apiKey)
            .enqueue(object :
                Callback<com.taufik.themovieshow.model.response.movie.cast.MovieCastResponse> {
                override fun onResponse(
                    call: Call<com.taufik.themovieshow.model.response.movie.cast.MovieCastResponse>,
                    response: Response<com.taufik.themovieshow.model.response.movie.cast.MovieCastResponse>
                ) {
                    if (response.isSuccessful) {
                        _listDetailCast.value =
                            response.body()?.cast as ArrayList<com.taufik.themovieshow.model.response.movie.cast.MovieCast>
                    }
                }

                override fun onFailure(
                    call: Call<com.taufik.themovieshow.model.response.movie.cast.MovieCastResponse>,
                    t: Throwable
                ) {
                }
            })
    }

    fun setDetailMovieVideo(id: Int) {
        apiInstance.getMovieVideo(id, apiKey)
            .enqueue(object :
                Callback<com.taufik.themovieshow.model.response.movie.video.MovieVideoResponse> {
                override fun onResponse(
                    call: Call<com.taufik.themovieshow.model.response.movie.video.MovieVideoResponse>,
                    response: Response<com.taufik.themovieshow.model.response.movie.video.MovieVideoResponse>
                ) {
                    if (response.isSuccessful) {
                        _detailVideo.value = response.body()
                    }
                }

                override fun onFailure(
                    call: Call<com.taufik.themovieshow.model.response.movie.video.MovieVideoResponse>,
                    t: Throwable
                ) {
                }
            })
    }

    fun setDetailMovieReviews(id: Int) {
        apiInstance.getReviewsMovie(id, apiKey)
            .enqueue(object :
                Callback<com.taufik.themovieshow.model.response.common.reviews.ReviewsResponse> {
                override fun onResponse(
                    call: Call<com.taufik.themovieshow.model.response.common.reviews.ReviewsResponse>,
                    response: Response<com.taufik.themovieshow.model.response.common.reviews.ReviewsResponse>
                ) {
                    if (response.isSuccessful) {
                        _listReviews.value =
                            response.body()?.results as ArrayList<com.taufik.themovieshow.model.response.common.reviews.ReviewsResult>
                    }
                }

                override fun onFailure(
                    call: Call<com.taufik.themovieshow.model.response.common.reviews.ReviewsResponse>,
                    t: Throwable
                ) {
                }
            })
    }

    fun setDetailMovieSimilar(id: Int) {
        apiInstance.getSimilarMovie(id, apiKey)
            .enqueue(object :
                Callback<com.taufik.themovieshow.model.response.movie.similar.MovieSimilarResponse> {
                override fun onResponse(
                    call: Call<com.taufik.themovieshow.model.response.movie.similar.MovieSimilarResponse>,
                    response: Response<com.taufik.themovieshow.model.response.movie.similar.MovieSimilarResponse>
                ) {
                    if (response.isSuccessful) {
                        _listSimilar.value =
                            response.body()?.results as ArrayList<com.taufik.themovieshow.model.response.movie.similar.MovieSimilarResult>
                    }
                }

                override fun onFailure(
                    call: Call<com.taufik.themovieshow.model.response.movie.similar.MovieSimilarResponse>,
                    t: Throwable
                ) {
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