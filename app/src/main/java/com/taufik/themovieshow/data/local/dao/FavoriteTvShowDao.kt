package com.taufik.themovieshow.data.local.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.taufik.themovieshow.data.local.entity.tvshow.FavoriteTvShow

@Dao
interface FavoriteTvShowDao {

    @Insert
    suspend fun addToFavorite(favoriteTvShow: FavoriteTvShow)

    @Query("SELECT * FROM favorite_tv_show")
    fun getFavoriteTvShow(): LiveData<List<FavoriteTvShow>>

    @Query("SELECT count(*) FROM favorite_tv_show WHERE favorite_tv_show.tvShowId = :tvShowId")
    suspend fun checkFavorite(tvShowId: Int): Int

    @Query("DELETE FROM favorite_tv_show WHERE favorite_tv_show.tvShowId = :tvShowId")
    suspend fun removeFromFavorite(tvShowId: Int): Int
}