package com.taufik.themovieshow.ui.main.movie.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.taufik.themovieshow.data.local.dao.FavoriteMovieDao
import com.taufik.themovieshow.data.local.entity.FavoriteMovie
import com.taufik.themovieshow.data.local.room.MovieDatabase
import com.taufik.themovieshow.data.repository.TheMovieShowRepository
import com.taufik.themovieshow.model.response.common.reviews.ReviewsResult
import com.taufik.themovieshow.model.response.movie.cast.MovieCast
import com.taufik.themovieshow.model.response.movie.detail.MovieDetailResponse
import com.taufik.themovieshow.model.response.movie.similar.MovieSimilarResult
import com.taufik.themovieshow.model.response.movie.video.MovieVideoResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailMovieViewModel @Inject constructor(
    application: Application,
    private val repository: TheMovieShowRepository
) : AndroidViewModel(application) {

    private val _listDetailMovies = MutableLiveData<MovieDetailResponse>()
    val detailMovies: LiveData<MovieDetailResponse> = _listDetailMovies

    private val _detailVideo = MutableLiveData<MovieVideoResponse>()
    val detailVideo: LiveData<MovieVideoResponse> = _detailVideo

    private val _listDetailCast = MutableLiveData<ArrayList<MovieCast>>()
    val listDetailCast: LiveData<ArrayList<MovieCast>> = _listDetailCast

    private val _listReviews = MutableLiveData<ArrayList<ReviewsResult>>()
    val listReviewMovie: LiveData<ArrayList<ReviewsResult>> = _listReviews

    private val _listSimilar = MutableLiveData<ArrayList<MovieSimilarResult>>()
    val listSimilarMovie: LiveData<ArrayList<MovieSimilarResult>> = _listSimilar

    private var movieDao: FavoriteMovieDao?
    private var movieDb: MovieDatabase? = MovieDatabase.getDatabase(context = application)

    init {
        movieDao = movieDb?.favoriteMovieDao()
    }

    suspend fun setDetailMovies(id: Int) = repository.getDetailMovie(id)

    suspend fun setDetailMovieCast(id: Int) = repository.getMovieCast(id)

    suspend fun setDetailMovieVideo(id: Int) = repository.getMovieVideo(id)

    suspend fun setDetailMovieReviews(id: Int) = repository.getMovieReviews(id)

    suspend fun setDetailMovieSimilar(id: Int) = repository.getSimilarMovie(id)

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