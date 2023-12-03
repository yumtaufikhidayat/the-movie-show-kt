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

class TheMovieShowRepository @Inject constructor(
    private val remoteDataSource: RemoteDataSource,
    private val localDataSource: LocalDataSource
) {
    fun getMovieTrendingDay() = remoteDataSource.getMovieTrendingDay()

    fun getMovieNowPlaying() = remoteDataSource.getMovieNowPlaying()

    fun getMovieUpcoming() = remoteDataSource.getMovieUpcoming()

    suspend fun getDiscoverMovie(query: String) = remoteDataSource.getDiscoverMovie(query)

    suspend fun getMovieVideo(movieId: Int) = remoteDataSource.getMovieVideo(movieId)

    suspend fun getMovieCast(movieId: Int) = remoteDataSource.getMovieCast(movieId)

    suspend fun getDetailMovie(movieId: Int) = remoteDataSource.getDetailMovie(movieId)

    suspend fun getMovieReviews(movieId: Int) = remoteDataSource.getMovieReviews(movieId)

    suspend fun getSimilarMovie(movieId: Int) = remoteDataSource.getSimilarMovie(movieId)

    fun getTvShowsAiringToday() = remoteDataSource.getTvShowsAiringToday()

    fun getTvShowsPopular() = remoteDataSource.getTvShowsPopular()

    fun getTvShowsTrending() = remoteDataSource.getTvShowsTrending()

    suspend fun getDiscoverTvShows(query: String) = remoteDataSource.getDiscoverTvShows(query)

    suspend fun getTvShowsVideo(tvId: Int) = remoteDataSource.getTvShowsVideo(tvId)

    suspend fun getTvShowsCast(tvId: Int) = remoteDataSource.getTvShowsCast(tvId)

    suspend fun getDetailTvShows(tvId: Int) = remoteDataSource.getDetailTvShows(tvId)

    suspend fun getTvShowsReviews(tvId: Int) = remoteDataSource.getTvShowsReviews(tvId)

    suspend fun getSimilarTvShows(tvId: Int) = remoteDataSource.getSimilarTvShows(tvId)

    suspend fun addMovieToFavorite(favoriteMovieEntity: FavoriteMovieEntity) =
        localDataSource.addMovieToFavorite(favoriteMovieEntity)

    fun getAllFavoriteMovie() = localDataSource.getAllFavoriteMovies()

    fun getFavoriteMoviesByTitle() = localDataSource.getFavoriteMoviesByTitle()

    fun getFavoriteMoviesByRelease() = localDataSource.getFavoriteMoviesByRelease()

    fun getFavoriteMoviesByRating() = localDataSource.getFavoriteMoviesByRating()

    fun getFavoriteMovieList(rawQuery: RawQuery) = localDataSource.getFavoriteMovies(rawQuery)

    suspend fun checkFavoriteMovie(movieId: Int) = localDataSource.checkFavoriteMovie(movieId)

    suspend fun removeMovieFromFavorite(movieId: Int) = localDataSource.removeMovieFromFavorite(movieId)

    suspend fun addTvShowToFavorite(favoriteTvShowEntity: FavoriteTvShowEntity) =
        localDataSource.addTvShowToFavorite(favoriteTvShowEntity)

    fun getAllFavoriteTvShows() = localDataSource.getAllFavoriteTvShows()

    fun getFavoriteTvShowsByTitle() = localDataSource.getFavoriteTvShowsByTitle()

    fun getFavoriteTvShowsByRelease() = localDataSource.getFavoriteTvShowsByRelease()

    fun getFavoriteTvShowsByRating() = localDataSource.getFavoriteTvShowsByRating()
    fun getFavoriteTvShows(rawQuery: RawQuery): LiveData<List<FavoriteMovieEntity>> {
        return localDataSource.getFavoriteTvShows(rawQuery)
    }

    suspend fun checkFavoriteTvShow(tvShowId: Int) = localDataSource.checkFavoriteTvShow(tvShowId)

    suspend fun removeTvShowFromFavorite(tvShowId: Int) = localDataSource.removeTvShowFromFavorite(tvShowId)

    fun getAboutAuthor(context: Context) = localDataSource.getAboutAuthor(context)

    fun getAboutApplication(context: Context) = localDataSource.getAboutApplication(context)

    fun getSortFiltering(): List<SortFiltering> = localDataSource.getSortFiltering()
}