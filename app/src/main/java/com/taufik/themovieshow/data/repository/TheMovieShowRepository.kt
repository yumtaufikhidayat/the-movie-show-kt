package com.taufik.themovieshow.data.repository

import android.content.Context
import com.taufik.themovieshow.data.local.entity.movie.FavoriteMovieEntity
import com.taufik.themovieshow.data.local.entity.tvshow.FavoriteTvShowEntity
import com.taufik.themovieshow.data.source.LocalDataSource
import com.taufik.themovieshow.data.source.RemoteDataSource
import javax.inject.Inject

class TheMovieShowRepository @Inject constructor(
    private val remoteDataSource: RemoteDataSource,
    private val localDataSource: LocalDataSource
) {
    fun getMovieNowPlaying() = remoteDataSource.getMovieNowPlaying()

    fun getMovieUpcoming() = remoteDataSource.getMovieUpcoming()

    fun getMovieTrendingDay() = remoteDataSource.getMovieTrendingDay()

    suspend fun getDiscoverMovie(query: String) = remoteDataSource.getDiscoverMovie(query)

    suspend fun getMovieVideo(movieId: Int) = remoteDataSource.getMovieVideo(movieId)

    suspend fun getMovieCast(movieId: Int) = remoteDataSource.getMovieCast(movieId)

    suspend fun getDetailMovie(movieId: Int) = remoteDataSource.getDetailMovie(movieId)

    suspend fun getMovieReviews(movieId: Int) = remoteDataSource.getMovieReviews(movieId)

    suspend fun getSimilarMovie(movieId: Int) = remoteDataSource.getSimilarMovie(movieId)

    fun getTvShowsAiringToday() = remoteDataSource.getTvShowsAiringToday()

    suspend fun getTvShowsPopular(page: Int) = remoteDataSource.getTvShowsPopular(page)

    fun getTvShowsTrending() = remoteDataSource.getTvShowsTrending()

    suspend fun getDiscoverTvShows(query: String) = remoteDataSource.getDiscoverTvShows(query)

    suspend fun getTvShowsVideo(tvId: Int) = remoteDataSource.getTvShowsVideo(tvId)

    suspend fun getTvShowsCast(tvId: Int) = remoteDataSource.getTvShowsCast(tvId)

    suspend fun getDetailTvShows(tvId: Int) = remoteDataSource.getDetailTvShows(tvId)

    suspend fun getTvShowsReviews(tvId: Int) = remoteDataSource.getTvShowsReviews(tvId)

    suspend fun getSimilarTvShows(tvId: Int) = remoteDataSource.getSimilarTvShows(tvId)

    suspend fun addMovieToFavorite(favoriteMovieEntity: FavoriteMovieEntity) =
        localDataSource.addMovieToFavorite(favoriteMovieEntity)

    fun getFavoriteMovie() = localDataSource.getFavoriteMovies()

    suspend fun checkFavoriteMovie(movieId: Int) = localDataSource.checkFavoriteMovie(movieId)

    suspend fun removeMovieFromFavorite(movieId: Int) =
        localDataSource.removeMovieFromFavorite(movieId)

    suspend fun addTvShowToFavorite(favoriteTvShowEntity: FavoriteTvShowEntity) =
        localDataSource.addTvShowToFavorite(favoriteTvShowEntity)

    fun getFavoriteTvShow() = localDataSource.getFavoriteTvShows()

    suspend fun checkFavoriteTvShow(tvShowId: Int) = localDataSource.checkFavoriteTvShow(tvShowId)

    suspend fun removeTvShowFromFavorite(tvShowId: Int) =
        localDataSource.removeTvShowFromFavorite(tvShowId)

    fun getAboutAuthor(context: Context) = localDataSource.getAboutAuthor(context)

    fun getAboutApplication(context: Context) = localDataSource.getAboutApplication(context)
}