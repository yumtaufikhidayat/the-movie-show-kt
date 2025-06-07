package com.taufik.themovieshow.ui.detail.movie_tvshow.viewmodel

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
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailMovieTvShowViewModel @Inject constructor(private val repository: TheMovieShowRepository) : ViewModel() {

    var idMovieTvShow = 0
    var titleMovieTvShow = ""

    // Movies
    fun detailMoviesResponse(movieId: Int): StateFlow<NetworkResult<MovieDetailResponse>> =
        repository.getDetailMovie(movieId).stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(DELAY_EMIT),
            initialValue = NetworkResult.Loading()
        )

    fun detailMoviesCastResponse(movieId: Int): StateFlow<NetworkResult<MovieCastResponse>> =
        repository.getMovieCast(movieId).stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(DELAY_EMIT),
            initialValue = NetworkResult.Loading()
        )

    fun detailMoviesVideoResponse(movieId: Int): StateFlow<NetworkResult<MovieVideoResponse>> =
        repository.getMovieVideo(movieId).stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(DELAY_EMIT),
            initialValue = NetworkResult.Loading()
        )


    fun detailMovieReviewsResponse(movieId: Int): StateFlow<NetworkResult<ReviewsResponse>> =
        repository.getMovieReviews(movieId).stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(DELAY_EMIT),
            initialValue = NetworkResult.Loading()
        )

    fun detailMovieSimilarResponse(movieId: Int): StateFlow<NetworkResult<MovieSimilarResponse>> =
        repository.getSimilarMovie(movieId).stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(DELAY_EMIT),
            initialValue = NetworkResult.Loading()
        )

    // Favorite Movie
    fun addMovieToFavorite(
        movieId: Int,
        posterPath: String,
        title: String,
        releaseDate: String,
        rating: Double
    ) {
        viewModelScope.launch {
            repository.addMovieToFavorite(FavoriteMovieEntity(movieId, posterPath, title, releaseDate, rating))
        }
    }

    suspend fun checkFavoriteMovie(movieId: Int) = repository.checkFavoriteMovie(movieId)

    fun removeMovieFromFavorite(movieId: Int) {
        viewModelScope.launch {
            repository.removeMovieFromFavorite(movieId)
        }
    }

    // TV Shows
    fun detailTvShowResponse(tvShowId: Int): StateFlow<NetworkResult<TvShowsPopularDetailResponse>> =
        repository.getDetailTvShows(tvShowId).stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(DELAY_EMIT),
            initialValue = NetworkResult.Loading()
        )

    fun detailTvShowCastResponse(tvShowId: Int): StateFlow<NetworkResult<TvShowsCastResponse>> =
        repository.getTvShowsCast(tvShowId).stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(DELAY_EMIT),
            initialValue = NetworkResult.Loading()
        )

    fun detailTvShowVideoResponse(tvShowId: Int): StateFlow<NetworkResult<TvShowsVideoResponse>> =
        repository.getTvShowsVideo(tvShowId).stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(DELAY_EMIT),
            initialValue = NetworkResult.Loading()
        )

    fun detailTvShowReviewsResponse(tvShowId: Int): StateFlow<NetworkResult<ReviewsResponse>> =
        repository.getTvShowsReviews(tvShowId).stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(DELAY_EMIT),
            initialValue = NetworkResult.Loading()
        )

    fun detailTvShowSimilarResponse(tvShowId: Int): StateFlow<NetworkResult<TvShowsSimilarResponse>> =
        repository.getSimilarTvShows(tvShowId).stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(DELAY_EMIT),
            initialValue = NetworkResult.Loading()
        )

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
        const val DELAY_EMIT = 5000L
    }
}