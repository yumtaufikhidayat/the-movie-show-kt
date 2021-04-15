package com.taufik.themovieshow.ui.feature.movie.data.local

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface FavoriteMovieDao {

    @Insert
    suspend fun addToFavorite(favoriteMovie: FavoriteMovie)

    @Query("SELECT * FROM favorite_movie")
    fun getFavoriteMovie(): LiveData<List<FavoriteMovie>>

    @Query("SELECT count(*) FROM favorite_movie WHERE favorite_movie.movieId = :movieId")
    suspend fun checkFavorite(movieId: Int): Int

    @Query("DELETE FROM favorite_movie WHERE favorite_movie.movieId = :movieId")
    suspend fun removeFromFavorite(movieId: Int): Int
}