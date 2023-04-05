package com.taufik.themovieshow.data.repository

import com.taufik.themovieshow.data.NetworkResult
import com.taufik.themovieshow.data.local.entity.movie.FavoriteMovie
import com.taufik.themovieshow.data.local.entity.tvshow.FavoriteTvShow
import com.taufik.themovieshow.data.source.BaseApiResponse
import com.taufik.themovieshow.data.source.LocalDataSource
import com.taufik.themovieshow.data.source.RemoteDataSource
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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class TheMovieShowRepository @Inject constructor(
    private val remoteDataSource: RemoteDataSource,
    private val localDataSource: LocalDataSource
): BaseApiResponse() {
    private val dispatchersIO = Dispatchers.IO

    suspend fun getMovieNowPlaying(page: Int) = remoteDataSource.getMovieNowPlaying(page)

    suspend fun getMovieUpcoming(page: Int) = remoteDataSource.getMovieUpcoming(page)

    suspend fun getMovieTrendingDay(page: Int) = remoteDataSource.getMovieTrendingDay(page)
    
    suspend fun getDiscoverMovie(query: String) : Flow<NetworkResult<DiscoverMovieResponse>> {
        return flow {
            emit(safeApiCall { remoteDataSource.getDiscoverMovie(query) })
        }.flowOn(dispatchersIO)
    }

    suspend fun getMovieVideo(movieId: Int) : Flow<NetworkResult<MovieVideoResponse>> {
        return flow {
            emit(safeApiCall { remoteDataSource.getMovieVideo(movieId) })
        }.flowOn(dispatchersIO)
    }

    suspend fun getMovieCast(movieId: Int) : Flow<NetworkResult<MovieCastResponse>> {
        return flow {
            emit(safeApiCall { remoteDataSource.getMovieCast(movieId) })
        }.flowOn(dispatchersIO)
    }

    suspend fun getDetailMovie(movieId: Int) : Flow<NetworkResult<MovieDetailResponse>> {
        return flow {
            emit(safeApiCall { remoteDataSource.getDetailMovie(movieId) })
        }.flowOn(dispatchersIO)
    }

    suspend fun getMovieReviews(movieId: Int) : Flow<NetworkResult<ReviewsResponse>> {
        return flow {
            emit(safeApiCall { remoteDataSource.getMovieReviews(movieId) })
        }.flowOn(dispatchersIO)
    }

    suspend fun getSimilarMovie(movieId: Int) : Flow<NetworkResult<MovieSimilarResponse>> {
        return flow {
            emit(safeApiCall { remoteDataSource.getSimilarMovie(movieId) })
        }.flowOn(dispatchersIO)
    }

    suspend fun getTvShowsAiringToday(page: Int) = remoteDataSource.getTvShowsAiringToday(page)

    suspend fun getTvShowsPopular(page: Int) = remoteDataSource.getTvShowsPopular(page)

    suspend fun getTvShowsTrending(page: Int) = remoteDataSource.getTvShowsTrending(page)

    suspend fun getDiscoverTvShows(query: String) : Flow<NetworkResult<DiscoverTvShowsResponse>> {
        return flow {
            emit(safeApiCall { remoteDataSource.getDiscoverTvShows(query) })
        }.flowOn(dispatchersIO)
    }

    suspend fun getTvShowsVideo(tvId: Int) : Flow<NetworkResult<TvShowsVideoResponse>> {
        return flow {
            emit(safeApiCall { remoteDataSource.getTvShowsVideo(tvId) })
        }.flowOn(dispatchersIO)
    }

    suspend fun getTvShowsCast(tvId: Int) : Flow<NetworkResult<TvShowsCastResponse>> {
        return flow {
            emit(safeApiCall { remoteDataSource.getTvShowsCast(tvId) })
        }.flowOn(dispatchersIO)
    }

    suspend fun getDetailTvShows(tvId: Int) : Flow<NetworkResult<TvShowsPopularDetailResponse>> {
        return flow {
            emit(safeApiCall { remoteDataSource.getDetailTvShows(tvId) })
        }.flowOn(dispatchersIO)
    }

    suspend fun getTvShowsReviews(tvId: Int) : Flow<NetworkResult<ReviewsResponse>> {
        return flow {
            emit(safeApiCall { remoteDataSource.getTvShowsReviews(tvId) })
        }.flowOn(dispatchersIO)
    }

    suspend fun getSimilarTvShows(tvId: Int) : Flow<NetworkResult<TvShowsSimilarResponse>> {
        return flow {
            emit(safeApiCall { remoteDataSource.getSimilarTvShows(tvId) })
        }.flowOn(dispatchersIO)
    }

    suspend fun addMovieToFavorite(favoriteMovie: FavoriteMovie) = localDataSource.addMovieToFavorite(favoriteMovie)

    fun getFavoriteMovie() = localDataSource.getFavoriteMovies()

    suspend fun checkFavoriteMovie(movieId: Int) = localDataSource.checkFavoriteMovie(movieId)

    suspend fun removeMovieFromFavorite(movieId: Int) = localDataSource.removeMovieFromFavorite(movieId)

    suspend fun addTvShowToFavorite(favoriteTvShow: FavoriteTvShow) = localDataSource.addTvShowToFavorite(favoriteTvShow)

    fun getFavoriteTvShow() = localDataSource.getFavoriteTvShows()

    suspend fun checkFavoriteTvShow(tvShowId: Int) = localDataSource.checkFavoriteTvShow(tvShowId)

    suspend fun removeTvShowFromFavorite(tvShowId: Int) = localDataSource.removeTvShowFromFavorite(tvShowId)

    fun getAboutAuthor() = localDataSource.getAboutAuthor()

    fun getAboutApplication() = localDataSource.getAboutApplication()
}