package com.taufik.themovieshow.data.source

import com.taufik.themovieshow.BuildConfig
import com.taufik.themovieshow.data.BaseApiResponse
import com.taufik.themovieshow.data.NetworkResult
import com.taufik.themovieshow.data.remote.api.ApiService
import com.taufik.themovieshow.model.response.common.reviews.ReviewsResponse
import com.taufik.themovieshow.model.response.movie.cast.MovieCastResponse
import com.taufik.themovieshow.model.response.movie.detail.MovieDetailResponse
import com.taufik.themovieshow.model.response.movie.discover.DiscoverMovieResponse
import com.taufik.themovieshow.model.response.movie.nowplayingupcoming.MovieMainResponse
import com.taufik.themovieshow.model.response.movie.similar.MovieSimilarResponse
import com.taufik.themovieshow.model.response.movie.trending.MovieTrendingResponse
import com.taufik.themovieshow.model.response.movie.video.MovieVideoResponse
import com.taufik.themovieshow.model.response.tvshow.cast.TvShowsCastResponse
import com.taufik.themovieshow.model.response.tvshow.detail.TvShowsPopularDetailResponse
import com.taufik.themovieshow.model.response.tvshow.discover.DiscoverTvShowsResponse
import com.taufik.themovieshow.model.response.tvshow.popularairingtoday.TvShowsMainResponse
import com.taufik.themovieshow.model.response.tvshow.similar.TvShowsSimilarResponse
import com.taufik.themovieshow.model.response.tvshow.trending.TvShowsTrendingResponse
import com.taufik.themovieshow.model.response.tvshow.video.TvShowsVideoResponse
import com.taufik.themovieshow.utils.objects.CommonConstants
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RemoteDataSource @Inject constructor(
    private val apiService: ApiService
): BaseApiResponse() {

    private val apiKey = if (BuildConfig.API_KEY.isEmpty()) "API_KEY is not set in BuildConfig." else CommonConstants.API_KEY

    fun getMovieTrendingDay(): Flow<NetworkResult<MovieTrendingResponse>> = 
        safeApiCall { apiService.getMovieTrendingDay(apiKey) }

    fun getMovieNowPlaying(): Flow<NetworkResult<MovieMainResponse>> = 
        safeApiCall { apiService.getMovieNowPlaying(apiKey) }

    fun getMovieUpcoming(): Flow<NetworkResult<MovieMainResponse>> =
        safeApiCall { apiService.getMovieUpcoming(apiKey) }

    fun getDiscoverMovie(query: String): Flow<NetworkResult<DiscoverMovieResponse>> =
        safeApiCall { apiService.getDiscoverMovie(apiKey, query) }
    
    fun getMovieVideo(movieId: Int) : Flow<NetworkResult<MovieVideoResponse>> =
        safeApiCall { apiService.getMovieVideo(movieId, apiKey) }

    fun getMovieCast(movieId: Int): Flow<NetworkResult<MovieCastResponse>> =
        safeApiCall { apiService.getMovieCast(movieId, apiKey) }

    fun getDetailMovie(movieId: Int) : Flow<NetworkResult<MovieDetailResponse>> =
        safeApiCall { apiService.getDetailMovie(movieId, apiKey) }

    fun getMovieReviews(movieId: Int) : Flow<NetworkResult<ReviewsResponse>> =
        safeApiCall { apiService.getReviewsMovie(movieId, apiKey) }

    fun getSimilarMovie(movieId: Int) : Flow<NetworkResult<MovieSimilarResponse>> =
        safeApiCall { apiService.getSimilarMovie(movieId, apiKey) }

    fun getTvShowsTrending(): Flow<NetworkResult<TvShowsTrendingResponse>> =
        safeApiCall { apiService.getTvShowsTrending(apiKey) }

    fun getTvShowsAiringToday(): Flow<NetworkResult<TvShowsMainResponse>> =
        safeApiCall { apiService.getTvShowsAiringToday(apiKey) }

    fun getTvShowsPopular(): Flow<NetworkResult<TvShowsMainResponse>> =
        safeApiCall { apiService.getTvShowsPopular(apiKey) }

    fun getDiscoverTvShows(query: String): Flow<NetworkResult<DiscoverTvShowsResponse>> =
        safeApiCall { apiService.getDiscoverTvShows(apiKey, query) }

    fun getTvShowsVideo(tvId: Int) : Flow<NetworkResult<TvShowsVideoResponse>> =
        safeApiCall { apiService.getTvShowsVideo(tvId, apiKey) }

    fun getTvShowsCast(tvId: Int) : Flow<NetworkResult<TvShowsCastResponse>> =
        safeApiCall { apiService.getTvShowsCast(tvId, apiKey) }

    fun getDetailTvShows(tvId: Int) : Flow<NetworkResult<TvShowsPopularDetailResponse>> =
        safeApiCall { apiService.getDetailTvShows(tvId, apiKey) }

    fun getTvShowsReviews(tvId: Int): Flow<NetworkResult<ReviewsResponse>> =
        safeApiCall { apiService.getReviewsTvShows(tvId, apiKey) }

    fun getSimilarTvShows(tvId: Int): Flow<NetworkResult<TvShowsSimilarResponse>> =
        safeApiCall { apiService.getSimilarTvShows(tvId, apiKey) }
}