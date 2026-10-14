package com.taufik.themovieshow.di

import android.content.Context
import com.google.firebase.analytics.FirebaseAnalytics
import com.taufik.themovieshow.utils.analytics.AnalyticsTracker
import com.taufik.themovieshow.utils.analytics.FirebaseAnalyticsTracker
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class AnalyticsModule {

    @Binds
    @Singleton
    abstract fun bindAnalyticsTracker(
        tracker: FirebaseAnalyticsTracker
    ): AnalyticsTracker

    companion object {
        @Provides
        @Singleton
        fun provideFirebaseAnalytics(
            @ApplicationContext context: Context
        ): FirebaseAnalytics = FirebaseAnalytics.getInstance(context)
    }
}
