package com.taufik.themovieshow.di

import android.content.Context
import androidx.room.Room
import com.taufik.themovieshow.data.local.room.TheMovieShowDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Singleton
    @Provides
    fun provideFavoriteDatabase(
        @ApplicationContext context: Context
    ) = Room.databaseBuilder(
        context, TheMovieShowDatabase::class.java, "the_movie_show_db"
    ).fallbackToDestructiveMigration().build()

    @Singleton
    @Provides
    fun provideDao(db: TheMovieShowDatabase) = db.getFavoriteTheMovieShowDao()
}