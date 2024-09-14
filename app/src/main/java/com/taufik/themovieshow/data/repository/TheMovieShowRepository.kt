package com.taufik.themovieshow.data.repository

import android.content.Context
import androidx.lifecycle.LiveData
import com.taufik.themovieshow.data.local.entity.movie.FavoriteMovieEntity
import com.taufik.themovieshow.data.local.entity.tvshow.FavoriteTvShowEntity
import com.taufik.themovieshow.data.source.LocalDataSource
import com.taufik.themovieshow.data.source.RawQuery
import com.taufik.themovieshow.data.source.RemoteDataSource
import com.taufik.themovieshow.model.favorite.SortFiltering
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TheMovieShowRepository @Inject constructor(
    private val remoteDataSource: RemoteDataSource,
    private val localDataSource: LocalDataSource
) {
    fun getMovieTrendingDay() = remoteDataSource.getMovieTrendingDay()

    fun getMovieNowPlaying() = remoteDataSource.getMovieNowPlaying()

    fun getMovieUpcoming() = remoteDataSource.getMovieUpcoming()

    fun getDiscoverMovie(query: String) = remoteDataSource.getDiscoverMovie(query)

    fun getMovieVideo(movieId: Int) = remoteDataSource.getMovieVideo(movieId)

    fun getMovieCast(movieId: Int) = remoteDataSource.getMovieCast(movieId)

    fun getDetailMovie(movieId: Int) = remoteDataSource.getDetailMovie(movieId)

    fun getMovieReviews(movieId: Int) = remoteDataSource.getMovieReviews(movieId)

    fun getSimilarMovie(movieId: Int) = remoteDataSource.getSimilarMovie(movieId)

    fun getTvShowsAiringToday() = remoteDataSource.getTvShowsAiringToday()

    fun getTvShowsPopular() = remoteDataSource.getTvShowsPopular()

    fun getTvShowsTrending() = remoteDataSource.getTvShowsTrending()

    fun getDiscoverTvShows(query: String) = remoteDataSource.getDiscoverTvShows(query)

    fun getTvShowsVideo(tvId: Int) = remoteDataSource.getTvShowsVideo(tvId)

    fun getTvShowsCast(tvId: Int) = remoteDataSource.getTvShowsCast(tvId)

    fun getDetailTvShows(tvId: Int) = remoteDataSource.getDetailTvShows(tvId)

    fun getTvShowsReviews(tvId: Int) = remoteDataSource.getTvShowsReviews(tvId)

    fun getSimilarTvShows(tvId: Int) = remoteDataSource.getSimilarTvShows(tvId)

    suspend fun addMovieToFavorite(favoriteMovieEntity: FavoriteMovieEntity) = localDataSource.addMovieToFavorite(favoriteMovieEntity)

    fun getFavoriteMovieList(rawQuery: RawQuery) = localDataSource.getFavoriteMovies(rawQuery)

    suspend fun checkFavoriteMovie(movieId: Int) = localDataSource.checkFavoriteMovie(movieId)

    suspend fun removeMovieFromFavorite(movieId: Int) = localDataSource.removeMovieFromFavorite(movieId)

    suspend fun addTvShowToFavorite(favoriteTvShowEntity: FavoriteTvShowEntity) = localDataSource.addTvShowToFavorite(favoriteTvShowEntity)

    fun getFavoriteTvShows(rawQuery: RawQuery): LiveData<List<FavoriteTvShowEntity>> = localDataSource.getFavoriteTvShows(rawQuery)

    suspend fun checkFavoriteTvShow(tvShowId: Int) = localDataSource.checkFavoriteTvShow(tvShowId)

    suspend fun removeTvShowFromFavorite(tvShowId: Int) = localDataSource.removeTvShowFromFavorite(tvShowId)

    fun getAboutAuthor(context: Context) = localDataSource.getAboutAuthor(context)

    fun getAboutApplication(context: Context) = localDataSource.getAboutApplication(context)

    fun getSortFiltering(): List<SortFiltering> = localDataSource.getSortFiltering()
}