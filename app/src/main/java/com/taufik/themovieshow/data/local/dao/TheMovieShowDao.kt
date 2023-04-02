package com.taufik.themovieshow.data.local.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.taufik.themovieshow.data.local.entity.movie.FavoriteMovie
import com.taufik.themovieshow.data.local.entity.tvshow.FavoriteTvShow

@Dao
interface TheMovieShowDao {
    @Insert
    suspend fun addMovieToFavorite(favoriteMovie: FavoriteMovie)

    @Query("SELECT * FROM favorite_movie")
    fun getFavoriteMovies(): LiveData<List<FavoriteMovie>>

    @Query("SELECT count(*) FROM favorite_movie WHERE favorite_movie.movieId = :movieId")
    suspend fun checkMovieFavorite(movieId: Int): Int

    @Query("DELETE FROM favorite_movie WHERE favorite_movie.movieId = :movieId")
    suspend fun removeMovieFromFavorite(movieId: Int): Int

    @Insert
    suspend fun addTvShowToFavorite(favoriteTvShow: FavoriteTvShow)

    @Query("SELECT * FROM favorite_tv_show")
    fun getFavoriteTvShows(): LiveData<List<FavoriteTvShow>>

    @Query("SELECT count(*) FROM favorite_tv_show WHERE favorite_tv_show.tvShowId = :tvShowId")
    suspend fun checkTvShowFavorite(tvShowId: Int): Int

    @Query("DELETE FROM favorite_tv_show WHERE favorite_tv_show.tvShowId = :tvShowId")
    suspend fun removeTvShowFromFavorite(tvShowId: Int): Int
}