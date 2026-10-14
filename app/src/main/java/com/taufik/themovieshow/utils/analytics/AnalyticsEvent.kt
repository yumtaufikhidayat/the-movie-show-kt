package com.taufik.themovieshow.utils.analytics

import com.google.firebase.analytics.FirebaseAnalytics
import com.taufik.themovieshow.utils.enums.DetailSource
import com.taufik.themovieshow.utils.enums.FROM

sealed class AnalyticsEvent(val name: String, val params: Map<String, String> = emptyMap()) {

    class ScreenView(screenName: String, screenClass: String) : AnalyticsEvent(
        FirebaseAnalytics.Event.SCREEN_VIEW,
        mapOf(
            FirebaseAnalytics.Param.SCREEN_NAME to screenName,
            FirebaseAnalytics.Param.SCREEN_CLASS to screenClass
        )
    )

    class SelectContent(id: Int, title: String, from: FROM, source: DetailSource) : AnalyticsEvent(
        FirebaseAnalytics.Event.SELECT_CONTENT,
        content(id, from) + mapOf(
            FirebaseAnalytics.Param.ITEM_NAME to title.limit(),
            PARAM_SOURCE to source.name.lowercase()
        )
    )

    class FavoriteAdd(id: Int, title: String, from: FROM) : AnalyticsEvent(
        "favorite_add",
        content(id, from) + (FirebaseAnalytics.Param.ITEM_NAME to title.limit())
    )

    class FavoriteRemove(id: Int, title: String, from: FROM) : AnalyticsEvent(
        "favorite_remove",
        content(id, from) + (FirebaseAnalytics.Param.ITEM_NAME to title.limit())
    )

    class Share(id: Int, from: FROM) : AnalyticsEvent(FirebaseAnalytics.Event.SHARE, content(id, from))

    class PlayTrailer(id: Int, from: FROM) : AnalyticsEvent("play_trailer", content(id, from))

    class ReviewClick(id: Int, from: FROM) : AnalyticsEvent("review_click", content(id, from))

    class ReadMoreClick(id: Int, from: FROM, isExpanded: Boolean) : AnalyticsEvent(
        "read_more_click",
        content(id, from) + (PARAM_STATE to if (isExpanded) "expanded" else "collapsed")
    )

    class OpenSearch(from: FROM) : AnalyticsEvent("open_search", contentType(from))

    class Search(term: String, from: FROM) : AnalyticsEvent(
        FirebaseAnalytics.Event.SEARCH,
        contentType(from) + (FirebaseAnalytics.Param.SEARCH_TERM to term.limit())
    )

    class RetryClick(screenName: String) : AnalyticsEvent(
        "retry_click",
        mapOf(FirebaseAnalytics.Param.SCREEN_NAME to screenName)
    )

    class TabSelect(tab: String) : AnalyticsEvent("tab_select", mapOf(PARAM_TAB to tab))

    class FavoriteSort(from: FROM, sortId: Int) : AnalyticsEvent(
        "favorite_sort",
        contentType(from) + (PARAM_SORT_TYPE to sortType(sortId))
    )

    class LanguageChange(languageCode: String) : AnalyticsEvent(
        "language_change",
        mapOf(PARAM_LANGUAGE to languageCode)
    )

    class AboutItemClick(item: String) : AnalyticsEvent("about_item_click", mapOf(PARAM_ITEM to item))

    override fun equals(other: Any?): Boolean =
        other is AnalyticsEvent && name == other.name && params == other.params

    override fun hashCode(): Int = 31 * name.hashCode() + params.hashCode()

    override fun toString(): String = "AnalyticsEvent(name=$name, params=$params)"

    companion object {
        const val USER_PROPERTY_BUILD_FLAVOR = "build_flavor"

        const val PARAM_SOURCE = "source"
        const val PARAM_STATE = "state"
        const val PARAM_TAB = "tab"
        const val PARAM_SORT_TYPE = "sort_type"
        const val PARAM_LANGUAGE = "language"
        const val PARAM_ITEM = "item"

        // Firebase limits event parameter values to 100 characters.
        private const val MAX_PARAM_VALUE_LENGTH = 100

        private fun String.limit() = take(MAX_PARAM_VALUE_LENGTH)

        private fun contentType(from: FROM) = mapOf(FirebaseAnalytics.Param.CONTENT_TYPE to from.name.lowercase())

        private fun content(id: Int, from: FROM) = contentType(from) + (FirebaseAnalytics.Param.ITEM_ID to id.toString())

        private fun sortType(sortId: Int) = when (sortId) {
            0 -> "all"
            1 -> "title"
            2 -> "release_date"
            3 -> "rating"
            else -> sortId.toString()
        }
    }
}
