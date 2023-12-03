package com.taufik.themovieshow.di

import android.content.Context
import androidx.room.Room
import com.taufik.themovieshow.data.local.dao.TheMovieShowDao
import com.taufik.themovieshow.data.local.room.TheMovieShowDatabase
import com.taufik.themovieshow.utils.CommonConstants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import net.sqlcipher.database.SQLiteDatabase
import net.sqlcipher.database.SupportFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Singleton
    @Provides
    fun provideFavoriteDatabase(
        @ApplicationContext context: Context
    ) : TheMovieShowDatabase {
        val passphrase = SQLiteDatabase.getBytes(CommonConstants.ENCRYPTED_DB_PASSPHRASE.toCharArray())
        val factory = SupportFactory(passphrase)
        return Room.databaseBuilder(
            context = context,
            klass = TheMovieShowDatabase::class.java,
            name = CommonConstants.DB_NAME
        ).fallbackToDestructiveMigration()
            .openHelperFactory(factory)
            .build()
    }

    @Singleton
    @Provides
    fun provideDao(db: TheMovieShowDatabase): TheMovieShowDao = db.getFavoriteTheMovieShowDao()
}