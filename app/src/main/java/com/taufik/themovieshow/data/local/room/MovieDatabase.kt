package com.taufik.themovieshow.data.local.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.taufik.themovieshow.data.local.dao.FavoriteMovieDao
import com.taufik.themovieshow.data.local.entity.FavoriteMovie

@Database(entities = [FavoriteMovie::class], version = 1, exportSchema = false)
abstract class MovieDatabase: RoomDatabase() {
    companion object {

        private var INSTANCE : MovieDatabase? = null

        fun getDatabase(context: Context): MovieDatabase? {
            if (INSTANCE == null) {
                synchronized(MovieDatabase::class){
                    INSTANCE = Room.databaseBuilder(
                        context.applicationContext,
                        MovieDatabase::class.java,
                        "movie_database"
                    ).fallbackToDestructiveMigration().build()
                }
            }

            return INSTANCE
        }
    }

    abstract fun favoriteMovieDao(): FavoriteMovieDao
}