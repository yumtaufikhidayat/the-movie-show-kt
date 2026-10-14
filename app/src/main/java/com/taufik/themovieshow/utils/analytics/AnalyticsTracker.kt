package com.taufik.themovieshow.utils.analytics

interface AnalyticsTracker {
    fun log(event: AnalyticsEvent)
    fun setUserProperty(name: String, value: String)
}
