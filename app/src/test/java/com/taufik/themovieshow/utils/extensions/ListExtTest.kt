package com.taufik.themovieshow.utils.extensions

import com.taufik.themovieshow.model.response.movie.video.MovieVideoResult
import com.taufik.themovieshow.model.response.tvshow.video.TvShowsVideoResult
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class ListExtTest {

    @Test
    fun `toMovieBaseVideoItemList with null returns empty list`() {
        val list: List<MovieVideoResult>? = null
        assertTrue(list.toMovieBaseVideoItemList().isEmpty())
    }

    @Test
    fun `toMovieBaseVideoItemList with empty list returns empty list`() {
        val list = emptyList<MovieVideoResult>()
        assertTrue(list.toMovieBaseVideoItemList().isEmpty())
    }

    @Test
    fun `toMovieBaseVideoItemList maps movie video fields correctly`() {
        val list = listOf(
            MovieVideoResult(
                id = "vid1",
                iso31661 = "US",
                iso6391 = "en",
                key = "abc123xyz",
                name = "Official Trailer",
                site = "YouTube",
                size = 1080,
                type = "Trailer"
            ),
            MovieVideoResult(
                id = null,
                iso31661 = null,
                iso6391 = null,
                key = null,
                name = null,
                site = null,
                size = 720,
                type = null
            )
        )

        val result = list.toMovieBaseVideoItemList()
        assertEquals(2, result.size)
        assertEquals("vid1", result[0].id)
        assertEquals("Official Trailer", result[0].name)
        assertEquals("abc123xyz", result[0].key)

        assertEquals("", result[1].id)
        assertEquals("", result[1].name)
        assertEquals("", result[1].key)
    }

    @Test
    fun `toTvShowBaseVideoItemList with null returns empty list`() {
        val list: List<TvShowsVideoResult>? = null
        assertTrue(list.toTvShowBaseVideoItemList().isEmpty())
    }

    @Test
    fun `toTvShowBaseVideoItemList maps tv show video fields correctly`() {
        val list = listOf(
            TvShowsVideoResult(
                id = "tv_vid1",
                iso31661 = "US",
                iso6391 = "en",
                key = "def456uvw",
                name = "Teaser",
                site = "YouTube",
                size = 1080,
                type = "Teaser"
            )
        )

        val result = list.toTvShowBaseVideoItemList()
        assertEquals(1, result.size)
        assertEquals("tv_vid1", result[0].id)
        assertEquals("Teaser", result[0].name)
        assertEquals("def456uvw", result[0].key)
    }
}
