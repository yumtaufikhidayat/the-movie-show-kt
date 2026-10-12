package com.taufik.themovieshow.di

import android.content.Context
import com.taufik.themovieshow.data.local.preferences.language.ILanguagePreference
import com.taufik.themovieshow.data.local.preferences.language.LanguagePreference
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
    fun provideLanguagePreference(
        @ApplicationContext context: Context
    ): ILanguagePreference = LanguagePreference(context)
}