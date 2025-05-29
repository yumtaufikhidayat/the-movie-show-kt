package com.taufik.themovieshow.ui.movie.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.taufik.themovieshow.data.NetworkResult
import com.taufik.themovieshow.data.local.entity.movie.FavoriteMovieEntity
import com.taufik.themovieshow.data.repository.TheMovieShowRepository
import com.taufik.themovieshow.model.response.common.reviews.ReviewsResponse
import com.taufik.themovieshow.model.response.movie.cast.MovieCastResponse
import com.taufik.themovieshow.model.response.movie.detail.MovieDetailResponse
import com.taufik.themovieshow.model.response.movie.similar.MovieSimilarResponse
import com.taufik.themovieshow.model.response.movie.video.MovieVideoResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailMovieViewModel @Inject constructor(private val repository: TheMovieShowRepository) : ViewModel() {

    var idMovie = 0
    var titleMovie = ""

    private val _detailMoviesResponse: MutableLiveData<NetworkResult<MovieDetailResponse>> = MutableLiveData()
    val detailMoviesResponse: LiveData<NetworkResult<MovieDetailResponse>> = _detailMoviesResponse

    private val _detailMoviesCastResponse: MutableLiveData<NetworkResult<MovieCastResponse>> = MutableLiveData()
    val detailMoviesCastResponse: LiveData<NetworkResult<MovieCastResponse>> = _detailMoviesCastResponse

    private val _detailMoviesVideoResponse: MutableLiveData<NetworkResult<MovieVideoResponse>> = MutableLiveData()
    val detailMoviesVideoResponse: LiveData<NetworkResult<MovieVideoResponse>> = _detailMoviesVideoResponse

    private val _detailMovieReviewsResponse: MutableLiveData<NetworkResult<ReviewsResponse>> = MutableLiveData()
    val detailMovieReviewsResponse: LiveData<NetworkResult<ReviewsResponse>> = _detailMovieReviewsResponse

    private val _detailMovieSimilarResponse: MutableLiveData<NetworkResult<MovieSimilarResponse>> = MutableLiveData()
    val detailMovieSimilarResponse: LiveData<NetworkResult<MovieSimilarResponse>> = _detailMovieSimilarResponse

    fun setDetailMovies(id: Int) = viewModelScope.launch {
        repository.getDetailMovie(id).collect {
            _detailMoviesResponse.value = it
        }
    }

    fun setDetailMovieCast(id: Int) = viewModelScope.launch {
        repository.getMovieCast(id).collect {
            _detailMoviesCastResponse.value = it
        }
    }

    fun setDetailMovieVideo(id: Int) = viewModelScope.launch {
        repository.getMovieVideo(id).collect {
            _detailMoviesVideoResponse.value = it
        }
    }

    fun setDetailMovieReviews(id: Int) = viewModelScope.launch {
        repository.getMovieReviews(id).collect {
            _detailMovieReviewsResponse.value = it
        }
    }

    fun setDetailMovieSimilar(id: Int) = viewModelScope.launch {
        repository.getSimilarMovie(id).collect {
            _detailMovieSimilarResponse.value = it
        }
    }

    // Favorite
    fun addMovieToFavorite(
        movieId: Int,
        posterPath: String,
        title: String,
        releaseDate: String,
        rating: Double
    ) {
        viewModelScope.launch {
            repository.addMovieToFavorite(
                FavoriteMovieEntity(
                    movieId, posterPath, title, releaseDate, rating
                )
            )
        }
    }

    suspend fun checkFavoriteMovie(movieId: Int) = repository.checkFavoriteMovie(movieId)

    fun removeMovieFromFavorite(movieId: Int) {
        viewModelScope.launch {
            repository.removeMovieFromFavorite(movieId)
        }
    }

    companion object {
        const val DELAY_SCROLL_TO_TOP_POSITION = 100L
    }
}