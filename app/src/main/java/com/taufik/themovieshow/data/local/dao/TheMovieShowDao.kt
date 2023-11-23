package com.taufik.themovieshow.data.local.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.taufik.themovieshow.data.local.entity.movie.FavoriteMovieEntity
import com.taufik.themovieshow.data.local.entity.tvshow.FavoriteTvShowEntity
import com.taufik.themovieshow.utils.CommonConstants

@Dao
interface TheMovieShowDao {
    @Insert
    suspend fun addMovieToFavorite(favoriteMovieEntity: FavoriteMovieEntity)

    @Query("SELECT * FROM ${CommonConstants.TABLE_NAME_FAVORITE_MOVIE_ENTITY}")
    fun getFavoriteMovies(): LiveData<List<FavoriteMovieEntity>>

    @Query("SELECT * FROM ${CommonConstants.TABLE_NAME_FAVORITE_MOVIE_ENTITY} " +
            "ORDER BY ${CommonConstants.COLUMN_NAME_TITLE}")
    fun getFavoriteMoviesByTitle(): LiveData<List<FavoriteMovieEntity>>

    @Query("SELECT * FROM ${CommonConstants.TABLE_NAME_FAVORITE_MOVIE_ENTITY} " +
            "ORDER BY ${CommonConstants.COLUMN_NAME_RELEASE_DATE}")
    fun getFavoriteMoviesByRelease(): LiveData<List<FavoriteMovieEntity>>

    @Query("SELECT * FROM ${CommonConstants.TABLE_NAME_FAVORITE_MOVIE_ENTITY} " +
            "ORDER BY ${CommonConstants.COLUMN_NAME_RATING}")
    fun getFavoriteMoviesByRating(): LiveData<List<FavoriteMovieEntity>>

    @Query(
        "SELECT count(*) FROM ${CommonConstants.TABLE_NAME_FAVORITE_MOVIE_ENTITY} " +
                "WHERE ${CommonConstants.TABLE_NAME_FAVORITE_MOVIE_ENTITY}.movieId = :movieId"
    )
    suspend fun checkMovieFavorite(movieId: Int): Int

    @Query(
        "DELETE FROM ${CommonConstants.TABLE_NAME_FAVORITE_MOVIE_ENTITY} " +
                "WHERE ${CommonConstants.TABLE_NAME_FAVORITE_MOVIE_ENTITY}.movieId = :movieId"
    )
    suspend fun removeMovieFromFavorite(movieId: Int): Int

    @Insert
    suspend fun addTvShowToFavorite(favoriteTvShowEntity: FavoriteTvShowEntity)

    @Query("SELECT * FROM ${CommonConstants.TABLE_NAME_FAVORITE_TV_SHOW_ENTITY} ORDER BY ${CommonConstants.COLUMN_NAME_TITLE} DESC")
    fun getFavoriteTvShows(): LiveData<List<FavoriteTvShowEntity>>

    @Query(
        "SELECT count(*) FROM ${CommonConstants.TABLE_NAME_FAVORITE_TV_SHOW_ENTITY} " +
                "WHERE ${CommonConstants.TABLE_NAME_FAVORITE_TV_SHOW_ENTITY}.tvShowId = :tvShowId"
    )
    suspend fun checkTvShowFavorite(tvShowId: Int): Int

    @Query(
        "DELETE FROM ${CommonConstants.TABLE_NAME_FAVORITE_TV_SHOW_ENTITY} " +
                "WHERE ${CommonConstants.TABLE_NAME_FAVORITE_TV_SHOW_ENTITY}.tvShowId = :tvShowId"
    )
    suspend fun removeTvShowFromFavorite(tvShowId: Int): Int
}