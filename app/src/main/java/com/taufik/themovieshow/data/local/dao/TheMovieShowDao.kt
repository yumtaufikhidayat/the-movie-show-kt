package com.taufik.themovieshow.data.local.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.RawQuery
import androidx.sqlite.db.SupportSQLiteQuery
import com.taufik.themovieshow.data.local.entity.movie.FavoriteMovieEntity
import com.taufik.themovieshow.data.local.entity.tvshow.FavoriteTvShowEntity
import com.taufik.themovieshow.utils.CommonConstants

@Dao
interface TheMovieShowDao {
    @Insert
    suspend fun addMovieToFavorite(favoriteMovieEntity: FavoriteMovieEntity)

    @RawQuery(observedEntities = [FavoriteMovieEntity::class])
    fun getFavoriteMovie(rawQuery: SupportSQLiteQuery) : LiveData<List<FavoriteMovieEntity>>

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

    @Query("SELECT * FROM ${CommonConstants.TABLE_NAME_FAVORITE_TV_SHOW_ENTITY}")
    fun getAllFavoriteTvShows(): LiveData<List<FavoriteTvShowEntity>>

    @Query("SELECT * FROM ${CommonConstants.TABLE_NAME_FAVORITE_TV_SHOW_ENTITY} " +
            "ORDER BY ${CommonConstants.COLUMN_NAME_TITLE}")
    fun getFavoriteTvShowsByTitle(): LiveData<List<FavoriteTvShowEntity>>

    @Query("SELECT * FROM ${CommonConstants.TABLE_NAME_FAVORITE_TV_SHOW_ENTITY} " +
            "ORDER BY ${CommonConstants.COLUMN_NAME_FIRST_AIR_DATE}")
    fun getFavoriteTvShowsByRelease(): LiveData<List<FavoriteTvShowEntity>>

    @Query("SELECT * FROM ${CommonConstants.TABLE_NAME_FAVORITE_TV_SHOW_ENTITY} " +
            "ORDER BY ${CommonConstants.COLUMN_NAME_RATING}")
    fun getFavoriteTvShowsByRating(): LiveData<List<FavoriteTvShowEntity>>

    @RawQuery(observedEntities = [FavoriteMovieEntity::class])
    fun getFavoriteTvShows(rawQuery: SupportSQLiteQuery) : LiveData<List<FavoriteMovieEntity>>


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