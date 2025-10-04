package com.taufik.themovieshow.di

import android.content.Context
import android.util.Log
import androidx.room.Room
import com.taufik.themovieshow.BuildConfig
import com.taufik.themovieshow.data.local.dao.TheMovieShowDao
import com.taufik.themovieshow.data.local.room.TheMovieShowDatabase
import com.taufik.themovieshow.utils.extensions.ensurePassphraseIntegrity
import com.taufik.themovieshow.utils.extensions.getDecryptedPassphrase
import com.taufik.themovieshow.utils.objects.CommonConstants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
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
        val passphrase = runBlocking { context.getDecryptedPassphrase() }
        val factory = SupportOpenHelperFactory(passphrase)
        val dbFile = context.getDatabasePath(CommonConstants.DB_NAME)

        runBlocking {
            delay(100)
            val isSamePassphrase = context.ensurePassphraseIntegrity(passphrase)
            if (!isSamePassphrase && dbFile.exists()) {
                if (BuildConfig.DEBUG) Log.w("DB", "Passphrase changed, resetting encrypted database...")
                dbFile.delete()
            }
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