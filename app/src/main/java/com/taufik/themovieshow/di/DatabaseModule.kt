package com.taufik.themovieshow.di

import android.content.Context
import android.util.Log
import androidx.room.Room
import com.taufik.themovieshow.data.local.dao.TheMovieShowDao
import com.taufik.themovieshow.data.local.room.TheMovieShowDatabase
import com.taufik.themovieshow.utils.objects.CommonConstants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import net.zetetic.database.sqlcipher.SupportOpenHelperFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Singleton
    @Provides
    fun provideFavoriteDatabase(
        @ApplicationContext context: Context
    ): TheMovieShowDatabase {
        val passphrase = CommonConstants.ENCRYPTED_DB_PASSPHRASE.toByteArray(Charsets.UTF_8)
        val factory = SupportOpenHelperFactory(passphrase)

        val dbFile = context.getDatabasePath(CommonConstants.DB_NAME)
        if (dbFile.exists()) {
            Log.w("DB", "Old DB exists, deleting for fresh start")
            dbFile.delete()
        }

        return Room.databaseBuilder(
            context,
            TheMovieShowDatabase::class.java,
            CommonConstants.DB_NAME
        )
            .fallbackToDestructiveMigration()
            .openHelperFactory(factory)
            .build()
    }

    @Singleton
    @Provides
    fun provideDao(db: TheMovieShowDatabase): TheMovieShowDao = db.getFavoriteTheMovieShowDao()
}