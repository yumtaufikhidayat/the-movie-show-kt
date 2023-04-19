package com.taufik.themovieshow.data.source

import android.content.Context
import com.taufik.themovieshow.data.local.dao.TheMovieShowDao
import com.taufik.themovieshow.data.local.entity.movie.FavoriteMovieEntity
import com.taufik.themovieshow.data.local.entity.tvshow.FavoriteTvShowEntity
import com.taufik.themovieshow.model.response.about.About
import com.taufik.themovieshow.utils.UtilsData
import javax.inject.Inject

class LocalDataSource @Inject constructor(
    private val favoriteDao: TheMovieShowDao
) {
    suspend fun addMovieToFavorite(favoriteMovieEntity: FavoriteMovieEntity) =
        favoriteDao.addMovieToFavorite(favoriteMovieEntity)

    fun getFavoriteMovies() = favoriteDao.getFavoriteMovies()

    suspend fun checkFavoriteMovie(movieId: Int) = favoriteDao.checkMovieFavorite(movieId)

    suspend fun removeMovieFromFavorite(movieId: Int) = favoriteDao.removeMovieFromFavorite(movieId)

    suspend fun addTvShowToFavorite(favoriteTvShowEntity: FavoriteTvShowEntity) =
        favoriteDao.addTvShowToFavorite(favoriteTvShowEntity)

    fun getFavoriteTvShows() = favoriteDao.getFavoriteTvShows()

    suspend fun checkFavoriteTvShow(tvShowId: Int) = favoriteDao.checkTvShowFavorite(tvShowId)

    suspend fun removeTvShowFromFavorite(tvShowId: Int) =
        favoriteDao.removeTvShowFromFavorite(tvShowId)

    fun getAboutAuthor(context: Context): List<About> = UtilsData.generateAboutAuthorData(context)

    fun getAboutApplication(context: Context): List<About> =
        UtilsData.generateAboutApplicationData(context)
}