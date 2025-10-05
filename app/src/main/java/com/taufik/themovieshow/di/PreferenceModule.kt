package com.taufik.themovieshow.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.dataStoreFile
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import com.taufik.themovieshow.data.local.preferences.language.ILanguagePreference
import com.taufik.themovieshow.data.local.preferences.language.LanguagePreference
import com.taufik.themovieshow.utils.objects.CommonConstants.KEY_SETTINGS_PREFS
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object PreferenceModule {

    @Provides
    @Singleton
    fun provideDataStore(@ApplicationContext context: Context): DataStore<Preferences> =
        PreferenceDataStoreFactory.create {
            context.dataStoreFile(KEY_SETTINGS_PREFS)
        }

    @Provides
    @Singleton
    fun provideLanguagePreference(
        @ApplicationContext context: Context
    ): ILanguagePreference = LanguagePreference(context)
}