package com.taufik.themovieshow.data.source

import android.content.Context
import androidx.lifecycle.LiveData
import com.taufik.themovieshow.data.local.dao.TheMovieShowDao
import com.taufik.themovieshow.data.local.entity.movie.FavoriteMovieEntity
import com.taufik.themovieshow.data.local.entity.tvshow.FavoriteTvShowEntity
import com.taufik.themovieshow.model.favorite.SortFiltering
import com.taufik.themovieshow.model.response.about.About
import com.taufik.themovieshow.utils.UtilsData
import com.taufik.themovieshow.utils.UtilsData.generateAboutApplicationData
import com.taufik.themovieshow.utils.UtilsData.generateAboutAuthorData
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LocalDataSource @Inject constructor(
    private val context: Context,
    private val favoriteDao: TheMovieShowDao,
) {
    suspend fun addMovieToFavorite(favoriteMovieEntity: FavoriteMovieEntity) =
        favoriteDao.addMovieToFavorite(favoriteMovieEntity)

    fun getFavoriteMovies(rawQuery: RawQuery): LiveData<List<FavoriteMovieEntity>> {
        return favoriteDao.getFavoriteMovie(rawQuery.value)
    }

    suspend fun checkFavoriteMovie(movieId: Int) = favoriteDao.checkMovieFavorite(movieId)

    suspend fun removeMovieFromFavorite(movieId: Int) = favoriteDao.removeMovieFromFavorite(movieId)

    suspend fun addTvShowToFavorite(favoriteTvShowEntity: FavoriteTvShowEntity) =
        favoriteDao.addTvShowToFavorite(favoriteTvShowEntity)

    fun getFavoriteTvShows(rawQuery: RawQuery): LiveData<List<FavoriteTvShowEntity>> {
        return favoriteDao.getFavoriteTvShows(rawQuery.value)
    }

    suspend fun checkFavoriteTvShow(tvShowId: Int) = favoriteDao.checkTvShowFavorite(tvShowId)

    suspend fun removeTvShowFromFavorite(tvShowId: Int) = favoriteDao.removeTvShowFromFavorite(tvShowId)

    fun getAboutAuthor(): List<About> = context.generateAboutAuthorData()

    fun getAboutApplication(): List<About> = context.generateAboutApplicationData()

    fun getSortFiltering(): List<SortFiltering> = UtilsData.generateSortFilteringData()
}