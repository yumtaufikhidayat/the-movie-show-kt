package com.taufik.themovieshow.utils.analytics

import com.taufik.themovieshow.utils.enums.DetailSource
import com.taufik.themovieshow.utils.enums.FROM
import org.junit.Assert.assertEquals
import org.junit.Test

class AnalyticsEventTest {

    private fun assertEvent(event: AnalyticsEvent, name: String, params: Map<String, String>) {
        assertEquals(name, event.name)
        assertEquals(params, event.params)
    }

    @Test
    fun `screen view uses the firebase screen params`() {
        assertEvent(
            AnalyticsEvent.ScreenView("movieFragment", "MovieFragment"),
            "screen_view",
            mapOf("screen_name" to "movieFragment", "screen_class" to "MovieFragment")
        )
    }

    @Test
    fun `select content carries the item, content type and source`() {
        assertEvent(
            AnalyticsEvent.SelectContent(19995, "Avatar", FROM.MOVIE, DetailSource.NOW_PLAYING),
            "select_content",
            mapOf(
                "content_type" to "movie",
                "item_id" to "19995",
                "item_name" to "Avatar",
                "source" to "now_playing"
            )
        )
    }

    @Test
    fun `favorite events carry the item and content type`() {
        val params = mapOf("content_type" to "tv_show", "item_id" to "7", "item_name" to "Shogun")

        assertEvent(AnalyticsEvent.FavoriteAdd(7, "Shogun", FROM.TV_SHOW), "favorite_add", params)
        assertEvent(AnalyticsEvent.FavoriteRemove(7, "Shogun", FROM.TV_SHOW), "favorite_remove", params)
    }

    @Test
    fun `detail click events carry the item and content type`() {
        val params = mapOf("content_type" to "movie", "item_id" to "42")

        assertEvent(AnalyticsEvent.Share(42, FROM.MOVIE), "share", params)
        assertEvent(AnalyticsEvent.PlayTrailer(42, FROM.MOVIE), "play_trailer", params)
        assertEvent(AnalyticsEvent.ReviewClick(42, FROM.MOVIE), "review_click", params)
        assertEvent(
            AnalyticsEvent.ReadMoreClick(42, FROM.MOVIE, isExpanded = true),
            "read_more_click",
            params + ("state" to "expanded")
        )
        assertEvent(
            AnalyticsEvent.ReadMoreClick(42, FROM.MOVIE, isExpanded = false),
            "read_more_click",
            params + ("state" to "collapsed")
        )
    }

    @Test
    fun `search events carry the content type`() {
        assertEvent(AnalyticsEvent.OpenSearch(FROM.TV_SHOW), "open_search", mapOf("content_type" to "tv_show"))
        assertEvent(
            AnalyticsEvent.Search("avatar", FROM.MOVIE),
            "search",
            mapOf("content_type" to "movie", "search_term" to "avatar")
        )
    }

    @Test
    fun `long text values are cut to the firebase limit`() {
        val longText = "a".repeat(150)

        assertEquals(100, AnalyticsEvent.Search(longText, FROM.MOVIE).params.getValue("search_term").length)
        assertEquals(
            100,
            AnalyticsEvent.SelectContent(1, longText, FROM.MOVIE, DetailSource.SEARCH).params.getValue("item_name").length
        )
    }

    @Test
    fun `favorite sort maps the sort id to a readable type`() {
        val expected = listOf("all", "title", "release_date", "rating", "9")

        val actual = listOf(0, 1, 2, 3, 9).map {
            AnalyticsEvent.FavoriteSort(FROM.MOVIE, it).params.getValue("sort_type")
        }

        assertEquals(expected, actual)
        assertEquals("favorite_sort", AnalyticsEvent.FavoriteSort(FROM.MOVIE, 0).name)
    }

    @Test
    fun `other ui events carry their own param`() {
        assertEvent(AnalyticsEvent.TabSelect("favoriteFragment"), "tab_select", mapOf("tab" to "favoriteFragment"))
        assertEvent(AnalyticsEvent.LanguageChange("in"), "language_change", mapOf("language" to "in"))
        assertEvent(AnalyticsEvent.AboutItemClick("github"), "about_item_click", mapOf("item" to "github"))
    }

    @Test
    fun `retry click carries the screen and section, plus the item on detail`() {
        assertEvent(
            AnalyticsEvent.RetryClick("discoverMovieFragment", section = "search"),
            "retry_click",
            mapOf("screen_name" to "discoverMovieFragment", "section" to "search")
        )
        assertEvent(
            AnalyticsEvent.RetryClick("detailMovieTvShowFragment", section = "reviews", id = 7, from = FROM.TV_SHOW),
            "retry_click",
            mapOf(
                "screen_name" to "detailMovieTvShowFragment",
                "section" to "reviews",
                "content_type" to "tv_show",
                "item_id" to "7"
            )
        )
    }

    @Test
    fun `events with the same name and params are equal`() {
        assertEquals(AnalyticsEvent.Share(1, FROM.MOVIE), AnalyticsEvent.Share(1, FROM.MOVIE))
    }
}
