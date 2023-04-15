package com.taufik.themovieshow.data.local.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.taufik.themovieshow.data.local.dao.TheMovieShowDao
import com.taufik.themovieshow.data.local.entity.movie.FavoriteMovieEntity
import com.taufik.themovieshow.data.local.entity.tvshow.FavoriteTvShowEntity

@Database(entities = [FavoriteMovieEntity::class, FavoriteTvShowEntity::class], version = 1, exportSchema = false)
abstract class TheMovieShowDatabase: RoomDatabase() {
    abstract fun getFavoriteTheMovieShowDao(): TheMovieShowDao
}