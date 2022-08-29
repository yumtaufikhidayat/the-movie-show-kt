package com.taufik.themovieshow.data.local.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.taufik.themovieshow.data.local.dao.FavoriteTvShowDao
import com.taufik.themovieshow.data.local.entity.FavoriteTvShow

@Database(entities = [FavoriteTvShow::class], version = 1)
abstract class TvShowDatabase: RoomDatabase() {
    companion object {

        private var INSTANCE : TvShowDatabase? = null

        fun getDatabase(context: Context): TvShowDatabase? {
            if (INSTANCE == null) {
                synchronized(TvShowDatabase::class){
                    INSTANCE = Room.databaseBuilder(
                        context.applicationContext,
                        TvShowDatabase::class.java,
                        "tv_show_database"
                    ).fallbackToDestructiveMigration().build()
                }
            }

            return INSTANCE
        }
    }

    abstract fun favoriteTvShowDao(): FavoriteTvShowDao
}