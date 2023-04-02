package com.taufik.themovieshow.data.local.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.taufik.themovieshow.data.local.dao.TheMovieShowDao
import com.taufik.themovieshow.data.local.entity.movie.FavoriteMovie
import com.taufik.themovieshow.data.local.entity.tvshow.FavoriteTvShow

@Database(entities = [FavoriteMovie::class, FavoriteTvShow::class], version = 1, exportSchema = false)
abstract class TheMovieShowDatabase: RoomDatabase() {
    abstract fun getFavoriteTheMovieShowDao(): TheMovieShowDao
}