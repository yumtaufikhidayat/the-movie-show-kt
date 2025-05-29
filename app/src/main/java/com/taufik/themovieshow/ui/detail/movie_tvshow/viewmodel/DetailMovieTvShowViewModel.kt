package com.taufik.themovieshow.ui.detail.movie_tvshow.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.taufik.themovieshow.data.NetworkResult
import com.taufik.themovieshow.data.local.entity.movie.FavoriteMovieEntity
import com.taufik.themovieshow.data.local.entity.tvshow.FavoriteTvShowEntity
import com.taufik.themovieshow.data.repository.TheMovieShowRepository
import com.taufik.themovieshow.model.response.common.reviews.ReviewsResponse
import com.taufik.themovieshow.model.response.movie.cast.MovieCastResponse
import com.taufik.themovieshow.model.response.movie.detail.MovieDetailResponse
import com.taufik.themovieshow.model.response.movie.similar.MovieSimilarResponse
import com.taufik.themovieshow.model.response.movie.video.MovieVideoResponse
import com.taufik.themovieshow.model.response.tvshow.cast.TvShowsCastResponse
import com.taufik.themovieshow.model.response.tvshow.detail.TvShowsPopularDetailResponse
import com.taufik.themovieshow.model.response.tvshow.similar.TvShowsSimilarResponse
import com.taufik.themovieshow.model.response.tvshow.video.TvShowsVideoResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailMovieTvShowViewModel @Inject constructor(private val repository: TheMovieShowRepository) : ViewModel() {

    var idMovieTvShow = 0
    var titleMovieTvShow = ""

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

    private val _detailTvShowResponse: MutableLiveData<NetworkResult<TvShowsPopularDetailResponse>> = MutableLiveData()
    val detailTvShowResponse: LiveData<NetworkResult<TvShowsPopularDetailResponse>> = _detailTvShowResponse

    private val _detailTvShowVideoResponse: MutableLiveData<NetworkResult<TvShowsVideoResponse>> = MutableLiveData()
    val detailTvShowVideoResponse: LiveData<NetworkResult<TvShowsVideoResponse>> = _detailTvShowVideoResponse

    private val _detailTvShowCastResponse: MutableLiveData<NetworkResult<TvShowsCastResponse>> = MutableLiveData()
    val detailTvShowCastResponse: LiveData<NetworkResult<TvShowsCastResponse>> = _detailTvShowCastResponse

    private val _detailTvShowReviewsResponse: MutableLiveData<NetworkResult<ReviewsResponse>> = MutableLiveData()
    val detailTvShowReviewsResponse: LiveData<NetworkResult<ReviewsResponse>> = _detailTvShowReviewsResponse

    private val _detailTvShowSimilarResponse: MutableLiveData<NetworkResult<TvShowsSimilarResponse>> = MutableLiveData()
    val detailTvShowSimilarResponse: LiveData<NetworkResult<TvShowsSimilarResponse>> = _detailTvShowSimilarResponse

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

    fun setDetailTvShow(id: Int) = viewModelScope.launch {
        repository.getDetailTvShows(id).collect {
            _detailTvShowResponse.value = it
        }
    }

    fun setDetailTvShowVideo(id: Int) = viewModelScope.launch {
        repository.getTvShowsVideo(id).collect {
            _detailTvShowVideoResponse.value = it
        }
    }

    fun setDetailTvShowsCast(id: Int) = viewModelScope.launch {
        repository.getTvShowsCast(id).collect {
            _detailTvShowCastResponse.value = it
        }
    }

    fun setDetailTvShowsReviews(id: Int) = viewModelScope.launch {
        repository.getTvShowsReviews(id).collect {
            _detailTvShowReviewsResponse.value = it
        }
    }

    fun setDetailTvShowsSimilar(id: Int) = viewModelScope.launch {
        repository.getSimilarTvShows(id).collect {
            _detailTvShowSimilarResponse.value = it
        }
    }

    // Favorite
    fun addTvShowFavorite(
        tvShowId: Int,
        posterPath: String,
        title: String,
        firstAirDate: String,
        rating: Double
    ) {
        viewModelScope.launch {
            repository.addTvShowToFavorite(
                FavoriteTvShowEntity(
                    tvShowId, posterPath, title, firstAirDate, rating
                )
            )
        }
    }

    suspend fun checkFavoriteTvShow(tvShowId: Int) = repository.checkFavoriteTvShow(tvShowId)

    fun removeTvShowFromFavorite(tvShowId: Int) {
        viewModelScope.launch {
            repository.removeTvShowFromFavorite(tvShowId)
        }
    }

    companion object {
        const val DELAY_SCROLL_TO_TOP_POSITION = 100L
    }
}