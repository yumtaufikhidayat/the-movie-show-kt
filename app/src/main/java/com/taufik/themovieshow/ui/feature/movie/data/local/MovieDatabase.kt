package com.taufik.themovieshow.ui.feature.movie.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [FavoriteMovie::class], version = 1)
abstract class MovieDatabase: RoomDatabase() {
    companion object {

        private var INSTANCE : MovieDatabase? = null

        fun getDatabase(context: Context): MovieDatabase? {
            if (INSTANCE == null) {
                synchronized(MovieDatabase::class){
                    INSTANCE = Room.databaseBuilder(context.applicationContext, MovieDatabase::class.java, "movie_database").build()
                }
            }

            return INSTANCE
        }
    }

    abstract fun favoriteMovieDao(): FavoriteMovieDao
}