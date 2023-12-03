package com.taufik.themovieshow.data.source

import android.content.Context
import androidx.lifecycle.LiveData
import com.taufik.themovieshow.data.local.dao.TheMovieShowDao
import com.taufik.themovieshow.data.local.entity.movie.FavoriteMovieEntity
import com.taufik.themovieshow.data.local.entity.tvshow.FavoriteTvShowEntity
import com.taufik.themovieshow.model.favorite.SortFiltering
import com.taufik.themovieshow.model.response.about.About
import com.taufik.themovieshow.utils.UtilsData
import javax.inject.Inject

class LocalDataSource @Inject constructor(
    private val favoriteDao: TheMovieShowDao
) {
    suspend fun addMovieToFavorite(favoriteMovieEntity: FavoriteMovieEntity) =
        favoriteDao.addMovieToFavorite(favoriteMovieEntity)

    fun getAllFavoriteMovies() = favoriteDao.getAllFavoriteMovies()

    fun getFavoriteMoviesByTitle() = favoriteDao.getFavoriteMoviesByTitle()

    fun getFavoriteMoviesByRelease() = favoriteDao.getFavoriteMoviesByRelease()

    fun getFavoriteMoviesByRating() = favoriteDao.getFavoriteMoviesByRating()

    fun getFavoriteMovies(rawQuery: RawQuery): LiveData<List<FavoriteMovieEntity>> {
        return favoriteDao.getFavoriteMovie(rawQuery.value)
    }

    suspend fun checkFavoriteMovie(movieId: Int) = favoriteDao.checkMovieFavorite(movieId)

    suspend fun removeMovieFromFavorite(movieId: Int) = favoriteDao.removeMovieFromFavorite(movieId)

    suspend fun addTvShowToFavorite(favoriteTvShowEntity: FavoriteTvShowEntity) =
        favoriteDao.addTvShowToFavorite(favoriteTvShowEntity)

    fun getAllFavoriteTvShows() = favoriteDao.getAllFavoriteTvShows()

    fun getFavoriteTvShowsByTitle() = favoriteDao.getFavoriteTvShowsByTitle()

    fun getFavoriteTvShowsByRelease() = favoriteDao.getFavoriteTvShowsByRelease()

    fun getFavoriteTvShowsByRating() = favoriteDao.getFavoriteTvShowsByRating()

    fun getFavoriteTvShows(rawQuery: RawQuery): LiveData<List<FavoriteMovieEntity>> {
        return favoriteDao.getFavoriteTvShows(rawQuery.value)
    }


    suspend fun checkFavoriteTvShow(tvShowId: Int) = favoriteDao.checkTvShowFavorite(tvShowId)

    suspend fun removeTvShowFromFavorite(tvShowId: Int) = favoriteDao.removeTvShowFromFavorite(tvShowId)

    fun getAboutAuthor(context: Context): List<About> = UtilsData.generateAboutAuthorData(context)

    fun getAboutApplication(context: Context): List<About> = UtilsData.generateAboutApplicationData(context)

    fun getSortFiltering(): List<SortFiltering> = UtilsData.generateSortFilteringData()
}