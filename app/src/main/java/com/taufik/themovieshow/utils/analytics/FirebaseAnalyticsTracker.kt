package com.taufik.themovieshow.utils.analytics

import android.os.Bundle
import android.util.Log
import com.google.firebase.analytics.FirebaseAnalytics
import com.taufik.themovieshow.BuildConfig
import javax.inject.Inject

class FirebaseAnalyticsTracker @Inject constructor(
    private val firebaseAnalytics: FirebaseAnalytics
) : AnalyticsTracker {

    override fun log(event: AnalyticsEvent) {
        val bundle = Bundle().apply {
            event.params.forEach { (key, value) -> putString(key, value) }
        }
        firebaseAnalytics.logEvent(event.name, bundle)
        // The Firebase SDK no longer prints event params, so print them in debug builds to verify events.
        if (BuildConfig.DEBUG) Log.d(TAG, "${event.name} ${event.params}")
    }

    override fun setUserProperty(name: String, value: String) {
        firebaseAnalytics.setUserProperty(name, value)
        if (BuildConfig.DEBUG) Log.d(TAG, "user property $name=$value")
    }

    private companion object {
        const val TAG = "AnalyticsTracker"
    }
}
