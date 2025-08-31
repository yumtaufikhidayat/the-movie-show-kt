package com.taufik.themovieshow.data.repository

import android.content.Context
import com.taufik.themovieshow.data.local.entity.movie.FavoriteMovieEntity
import com.taufik.themovieshow.data.local.entity.tvshow.FavoriteTvShowEntity
import com.taufik.themovieshow.data.source.LocalDataSource
import com.taufik.themovieshow.data.source.RawQuery
import com.taufik.themovieshow.data.source.RemoteDataSource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TheMovieShowRepository @Inject constructor(
    private val remoteDataSource: RemoteDataSource,
    private val localDataSource: LocalDataSource
): ITheMovieShowRepository {
    val languageFlow: Flow<String> = localDataSource.languageFlow

    override fun getMovieTrendingDay() = remoteDataSource.getMovieTrendingDay()

    override fun getMovieNowPlaying() = remoteDataSource.getMovieNowPlaying()

    override fun getMovieUpcoming() = remoteDataSource.getMovieUpcoming()

    override fun getDiscoverMovie(query: String) = remoteDataSource.getDiscoverMovie(query)

    override fun getMovieVideo(movieId: Int) = remoteDataSource.getMovieVideo(movieId)

    override fun getMovieCast(movieId: Int) = remoteDataSource.getMovieCast(movieId)

    override fun getDetailMovie(movieId: Int) = remoteDataSource.getDetailMovie(movieId)

    override fun getMovieReviews(movieId: Int) = remoteDataSource.getMovieReviews(movieId)

    override fun getSimilarMovie(movieId: Int) = remoteDataSource.getSimilarMovie(movieId)

    override fun getTvShowsAiringToday() = remoteDataSource.getTvShowsAiringToday()

    override fun getTvShowsPopular() = remoteDataSource.getTvShowsPopular()

    override fun getTvShowsTrending() = remoteDataSource.getTvShowsTrending()

    override fun getDiscoverTvShows(query: String) = remoteDataSource.getDiscoverTvShows(query)

    override fun getTvShowsVideo(tvId: Int) = remoteDataSource.getTvShowsVideo(tvId)

    override fun getTvShowsCast(tvId: Int) = remoteDataSource.getTvShowsCast(tvId)

    override fun getDetailTvShows(tvId: Int) = remoteDataSource.getDetailTvShows(tvId)

    override fun getTvShowsReviews(tvId: Int) = remoteDataSource.getTvShowsReviews(tvId)

    override fun getSimilarTvShows(tvId: Int) = remoteDataSource.getSimilarTvShows(tvId)

    override suspend fun addMovieToFavorite(favoriteMovieEntity: FavoriteMovieEntity) = localDataSource.addMovieToFavorite(favoriteMovieEntity)

    override fun getFavoriteMovieList(rawQuery: RawQuery) = localDataSource.getFavoriteMovies(rawQuery)

    override suspend fun checkFavoriteMovie(movieId: Int) = localDataSource.checkFavoriteMovie(movieId)

    override suspend fun removeMovieFromFavorite(movieId: Int) = localDataSource.removeMovieFromFavorite(movieId)

    override suspend fun addTvShowToFavorite(favoriteTvShowEntity: FavoriteTvShowEntity) = localDataSource.addTvShowToFavorite(favoriteTvShowEntity)

    override fun getFavoriteTvShows(rawQuery: RawQuery) = localDataSource.getFavoriteTvShows(rawQuery)

    override suspend fun checkFavoriteTvShow(tvShowId: Int) = localDataSource.checkFavoriteTvShow(tvShowId)

    override suspend fun removeTvShowFromFavorite(tvShowId: Int) = localDataSource.removeTvShowFromFavorite(tvShowId)

    override fun getAboutData(context: Context) = localDataSource.getAboutData(context)

    override fun getSortFiltering() = localDataSource.getSortFiltering()

    override suspend fun setLanguage(code: String, isChanged: Boolean) = localDataSource.setLanguage(code, isChanged)

    override suspend fun getLanguage()= localDataSource.getLanguage()

    override suspend fun setLanguageChangedMessage(isChanged: Boolean) = localDataSource.setLanguageChangedMessage(isChanged)

    override fun getLanguageChangedMessage() =  localDataSource.getLanguageChangedMessage()
}