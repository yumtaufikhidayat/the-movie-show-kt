package com.taufik.themovieshow.utils.analytics

class FakeAnalyticsTracker : AnalyticsTracker {

    val events = mutableListOf<AnalyticsEvent>()
    val userProperties = mutableMapOf<String, String>()

    override fun log(event: AnalyticsEvent) {
        events += event
    }

    override fun setUserProperty(name: String, value: String) {
        userProperties[name] = value
    }
}
