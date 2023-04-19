package com.taufik.themovieshow.data.source

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.liveData
import com.taufik.themovieshow.data.NetworkResult
import com.taufik.themovieshow.data.paging.movie.MovieNowPlayingPagingSource
import com.taufik.themovieshow.data.paging.movie.MovieTrendingPagingSource
import com.taufik.themovieshow.data.remote.api.ApiService
import com.taufik.themovieshow.model.response.common.reviews.ReviewsResponse
import com.taufik.themovieshow.model.response.movie.cast.MovieCastResponse
import com.taufik.themovieshow.model.response.movie.detail.MovieDetailResponse
import com.taufik.themovieshow.model.response.movie.discover.DiscoverMovieResponse
import com.taufik.themovieshow.model.response.movie.similar.MovieSimilarResponse
import com.taufik.themovieshow.model.response.movie.video.MovieVideoResponse
import com.taufik.themovieshow.model.response.tvshow.cast.TvShowsCastResponse
import com.taufik.themovieshow.model.response.tvshow.detail.TvShowsPopularDetailResponse
import com.taufik.themovieshow.model.response.tvshow.discover.DiscoverTvShowsResponse
import com.taufik.themovieshow.model.response.tvshow.similar.TvShowsSimilarResponse
import com.taufik.themovieshow.model.response.tvshow.video.TvShowsVideoResponse
import com.taufik.themovieshow.utils.CommonConstants
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class RemoteDataSource @Inject constructor(private val apiService: ApiService): BaseApiResponse() {
    private val dispatchersIO = Dispatchers.IO

    fun getMovieNowPlaying() = Pager(
        PagingConfig(
            pageSize = CommonConstants.STARTING_PAGE_INDEX,
            maxSize = CommonConstants.LOAD_PER_PAGE,
            enablePlaceholders = false
        ), pagingSourceFactory = {
            MovieNowPlayingPagingSource(apiService)
        }).liveData

    suspend fun getMovieUpcoming(page: Int) = apiService.getMovieUpcoming(page)

    fun getMovieTrendingDay() = Pager(
        PagingConfig(
            pageSize = CommonConstants.STARTING_PAGE_INDEX,
            maxSize = CommonConstants.LOAD_PER_PAGE,
            enablePlaceholders = false
        ), pagingSourceFactory = {
            MovieTrendingPagingSource(apiService)
        }).liveData

    suspend fun getDiscoverMovie(query: String): Flow<NetworkResult<DiscoverMovieResponse>> {
        return flow {
            emit(safeApiCall { apiService.getDiscoverMovie(query) })
        }.flowOn(dispatchersIO)
    }
    suspend fun getMovieVideo(movieId: Int) : Flow<NetworkResult<MovieVideoResponse>> {
        return flow {
            emit(safeApiCall { apiService.getMovieVideo(movieId) })
        }.flowOn(dispatchersIO)
    }

    suspend fun getMovieCast(movieId: Int): Flow<NetworkResult<MovieCastResponse>> {
        return flow {
            emit(safeApiCall { apiService.getMovieCast(movieId) })
        }.flowOn(dispatchersIO)
    }

    suspend fun getDetailMovie(movieId: Int) : Flow<NetworkResult<MovieDetailResponse>> {
        return flow {
            emit(safeApiCall { apiService.getDetailMovie(movieId) })
        }.flowOn(dispatchersIO)
    }

    suspend fun getMovieReviews(movieId: Int) : Flow<NetworkResult<ReviewsResponse>> {
        return flow {
            emit(safeApiCall { apiService.getReviewsMovie(movieId) })
        }.flowOn(dispatchersIO)
    }

    suspend fun getSimilarMovie(movieId: Int) : Flow<NetworkResult<MovieSimilarResponse>> {
        return flow {
            emit(safeApiCall { apiService.getSimilarMovie(movieId) })
        }.flowOn(dispatchersIO)
    }

    suspend fun getTvShowsAiringToday(page: Int) = apiService.getTvShowsAiringToday(page)

    suspend fun getTvShowsPopular(page: Int) = apiService.getTvShowsPopular(page)

    suspend fun getTvShowsTrending(page: Int) = apiService.getTvShowsTrending(page)

    suspend fun getDiscoverTvShows(query: String): Flow<NetworkResult<DiscoverTvShowsResponse>> {
        return flow {
            emit(safeApiCall { apiService.getDiscoverTvShows(query) })
        }.flowOn(dispatchersIO)
    }

    suspend fun getTvShowsVideo(tvId: Int) : Flow<NetworkResult<TvShowsVideoResponse>> {
        return flow {
            emit(safeApiCall { apiService.getTvShowsVideo(tvId) })
        }.flowOn(dispatchersIO)
    }

    suspend fun getTvShowsCast(tvId: Int) : Flow<NetworkResult<TvShowsCastResponse>> {
        return flow {
            emit(safeApiCall { apiService.getTvShowsCast(tvId) })
        }.flowOn(dispatchersIO)
    }

    suspend fun getDetailTvShows(tvId: Int) : Flow<NetworkResult<TvShowsPopularDetailResponse>> {
        return flow {
            emit(safeApiCall { apiService.getDetailTvShows(tvId) })
        }.flowOn(dispatchersIO)
    }

    suspend fun getTvShowsReviews(tvId: Int): Flow<NetworkResult<ReviewsResponse>> {
        return flow {
            emit(safeApiCall { apiService.getReviewsTvShows(tvId) })
        }.flowOn(dispatchersIO)
    }

    suspend fun getSimilarTvShows(tvId: Int): Flow<NetworkResult<TvShowsSimilarResponse>> {
        return flow {
            emit(safeApiCall { apiService.getSimilarTvShows(tvId) })
        }.flowOn(dispatchersIO)
    }
}