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
import com.taufik.themovieshow.utils.CommonConstants
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RemoteDataSource @Inject constructor(private val apiService: ApiService): BaseApiResponse() {

    private val dispatchersIO = Dispatchers.IO
    private val apiKey =
        if (BuildConfig.API_KEY.isEmpty()) "API_KEY is not set in BuildConfig."
        else CommonConstants.API_KEY

    fun getMovieTrendingDay(): Flow<NetworkResult<MovieTrendingResponse>> {
        return flow {
            emit(safeApiCall { apiService.getMovieTrendingDay(apiKey) })
        }.flowOn(dispatchersIO)
    }

    fun getMovieNowPlaying(): Flow<NetworkResult<MovieMainResponse>> {
        return flow {
            emit(safeApiCall { apiService.getMovieNowPlaying(apiKey) })
        }.flowOn(dispatchersIO)
    }

    fun getMovieUpcoming(): Flow<NetworkResult<MovieMainResponse>> {
        return flow {
            emit(safeApiCall { apiService.getMovieUpcoming(apiKey) })
        }.flowOn(dispatchersIO)
    }

    fun getDiscoverMovie(query: String): Flow<NetworkResult<DiscoverMovieResponse>> {
        return flow {
            emit(safeApiCall { apiService.getDiscoverMovie(apiKey, query) })
        }.flowOn(dispatchersIO)
    }
    fun getMovieVideo(movieId: Int) : Flow<NetworkResult<MovieVideoResponse>> {
        return flow {
            emit(safeApiCall { apiService.getMovieVideo(movieId, apiKey) })
        }.flowOn(dispatchersIO)
    }

    fun getMovieCast(movieId: Int): Flow<NetworkResult<MovieCastResponse>> {
        return flow {
            emit(safeApiCall { apiService.getMovieCast(movieId, apiKey) })
        }.flowOn(dispatchersIO)
    }

    fun getDetailMovie(movieId: Int) : Flow<NetworkResult<MovieDetailResponse>> {
        return flow {
            emit(safeApiCall { apiService.getDetailMovie(movieId, apiKey) })
        }.flowOn(dispatchersIO)
    }

    fun getMovieReviews(movieId: Int) : Flow<NetworkResult<ReviewsResponse>> {
        return flow {
            emit(safeApiCall { apiService.getReviewsMovie(movieId, apiKey) })
        }.flowOn(dispatchersIO)
    }

    fun getSimilarMovie(movieId: Int) : Flow<NetworkResult<MovieSimilarResponse>> {
        return flow {
            emit(safeApiCall { apiService.getSimilarMovie(movieId, apiKey) })
        }.flowOn(dispatchersIO)
    }

    fun getTvShowsTrending(): Flow<NetworkResult<TvShowsTrendingResponse>> {
        return flow {
            emit(safeApiCall { apiService.getTvShowsTrending(apiKey) })
        }.flowOn(dispatchersIO)
    }

    fun getTvShowsAiringToday(): Flow<NetworkResult<TvShowsMainResponse>> {
        return flow {
            emit(safeApiCall { apiService.getTvShowsAiringToday(apiKey) })
        }.flowOn(dispatchersIO)
    }

    fun getTvShowsPopular(): Flow<NetworkResult<TvShowsMainResponse>> {
        return flow {
            emit(safeApiCall { apiService.getTvShowsPopular(apiKey) })
        }.flowOn(dispatchersIO)
    }

    fun getDiscoverTvShows(query: String): Flow<NetworkResult<DiscoverTvShowsResponse>> {
        return flow {
            emit(safeApiCall { apiService.getDiscoverTvShows(apiKey, query) })
        }.flowOn(dispatchersIO)
    }

    fun getTvShowsVideo(tvId: Int) : Flow<NetworkResult<TvShowsVideoResponse>> {
        return flow {
            emit(safeApiCall { apiService.getTvShowsVideo(tvId, apiKey) })
        }.flowOn(dispatchersIO)
    }

    fun getTvShowsCast(tvId: Int) : Flow<NetworkResult<TvShowsCastResponse>> {
        return flow {
            emit(safeApiCall { apiService.getTvShowsCast(tvId, apiKey) })
        }.flowOn(dispatchersIO)
    }

    fun getDetailTvShows(tvId: Int) : Flow<NetworkResult<TvShowsPopularDetailResponse>> {
        return flow {
            emit(safeApiCall { apiService.getDetailTvShows(tvId, apiKey) })
        }.flowOn(dispatchersIO)
    }

    fun getTvShowsReviews(tvId: Int): Flow<NetworkResult<ReviewsResponse>> {
        return flow {
            emit(safeApiCall { apiService.getReviewsTvShows(tvId, apiKey) })
        }.flowOn(dispatchersIO)
    }

    fun getSimilarTvShows(tvId: Int): Flow<NetworkResult<TvShowsSimilarResponse>> {
        return flow {
            emit(safeApiCall { apiService.getSimilarTvShows(tvId, apiKey) })
        }.flowOn(dispatchersIO)
    }
}