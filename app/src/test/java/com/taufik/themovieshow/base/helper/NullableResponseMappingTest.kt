package com.taufik.themovieshow.base.helper

import com.google.gson.Gson
import com.taufik.themovieshow.model.response.common.reviews.ReviewsResult
import com.taufik.themovieshow.model.response.movie.cast.MovieCast
import com.taufik.themovieshow.model.response.movie.detail.MovieDetailResponse
import com.taufik.themovieshow.model.response.movie.similar.MovieSimilarResult
import com.taufik.themovieshow.model.response.movie.video.MovieVideoResult
import com.taufik.themovieshow.model.response.tvshow.detail.TvShowsPopularDetailResponse
import com.taufik.themovieshow.utils.enums.getUserCountryCertification
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

/**
 * TMDB sends null (or omits) fields that the app reads. Gson ignores Kotlin null-safety,
 * so these tests parse such payloads with Gson and check that mapping never throws.
 */
class NullableResponseMappingTest {

    private val gson = Gson()

    private inline fun <reified T> parse(json: String): T = gson.fromJson(json, T::class.java)

    @Test
    fun `movie detail with null fields maps to empty values`() {
        val response = parse<MovieDetailResponse>(
            """
            {
              "id": 1, "title": null, "release_date": null, "status": null, "overview": null,
              "poster_path": null, "backdrop_path": null, "homepage": null,
              "belongs_to_collection": null, "genres": null, "spoken_languages": null,
              "release_dates": null, "vote_average": null, "runtime": null
            }
            """
        )
        val data = MovieDetailWrapper(response)

        assertEquals("", data.titleText)
        assertEquals("", data.releaseDateText)
        assertEquals("", data.statusText)
        assertEquals("", data.overview)
        assertNull(data.posterPath)
        assertTrue(data.genres.isEmpty())
        assertTrue(data.spokenLanguages.isEmpty())
        assertEquals(0.0, data.voteAverage, 0.0)
        assertEquals("-", data.getUserCountryCertification())
        response.hashCode()
    }

    @Test
    fun `null entries inside detail lists are skipped`() {
        val response = parse<MovieDetailResponse>(
            """
            {
              "genres": [{"id": 1, "name": null}, {"id": 2, "name": "Drama"}],
              "spoken_languages": [{"english_name": null}, {"english_name": "English"}],
              "release_dates": {"results": [
                {"iso_3166_1": null, "release_dates": null},
                {"iso_3166_1": "US", "release_dates": [{"certification": null}, {"certification": "PG-13"}]}
              ]}
            }
            """
        )
        val data = MovieDetailWrapper(response)

        assertEquals(listOf("Drama"), data.genres)
        assertEquals(listOf("English"), data.spokenLanguages)
        assertEquals("PG-13", data.getUserCountryCertification())
    }

    @Test
    fun `tv show detail with null and missing fields maps to empty values`() {
        val response = parse<TvShowsPopularDetailResponse>(
            """
            {
              "id": 1, "name": null, "first_air_date": null, "last_air_date": null,
              "last_episode_to_air": null, "next_episode_to_air": null,
              "networks": null, "genres": null
            }
            """
        )
        val data = TvShowDetailWrapper(response)

        assertEquals("", data.titleText)
        assertEquals("", data.releaseDateText)
        assertEquals("", data.statusText)
        assertEquals("", data.overview)
        assertTrue(data.genres.isEmpty())
        assertTrue(data.spokenLanguages.isEmpty())
        assertNull(response.networks)
        response.hashCode()
    }

    @Test
    fun `review without author details maps to empty values`() {
        val review = parse<ReviewsResult>(
            """{"id": null, "author": null, "author_details": null, "content": null, "updated_at": null, "url": null}"""
        ).toBaseReviewItem()

        assertEquals("", review.id)
        assertEquals("", review.author)
        assertNull(review.authorAvatar)
        assertEquals("", review.authorRating)
        assertEquals("", review.updatedAt)
        assertEquals("", review.content)
        assertEquals("", review.url)
    }

    @Test
    fun `review with null avatar and rating keeps the rest`() {
        val review = parse<ReviewsResult>(
            """{"author": "Dean", "author_details": {"avatar_path": null, "rating": null}}"""
        ).toBaseReviewItem()

        assertEquals("Dean", review.author)
        assertNull(review.authorAvatar)
        assertEquals("", review.authorRating)
    }

    @Test
    fun `review rating keeps the value sent by TMDB`() {
        val rated = parse<ReviewsResult>("""{"author_details": {"rating": 8.0}}""").toBaseReviewItem()
        assertEquals("8.0", rated.authorRating)

        val zero = parse<ReviewsResult>("""{"author_details": {"rating": 0.0}}""").toBaseReviewItem()
        assertEquals("0.0", zero.authorRating)
    }

    @Test
    fun `cast, similar, and video items with missing fields map to empty values`() {
        val cast = parse<MovieCast>("{}").toCastItem()
        assertEquals("", cast.name)
        assertEquals("", cast.character)
        assertNull(cast.profilePath)

        val similar = parse<MovieSimilarResult>("{}").toMovieBaseSimilarItem()
        assertEquals("", similar.titleText)
        assertEquals("", similar.releaseDateText)

        val video = parse<MovieVideoResult>("""{"key": null, "name": null}""").toMovieVideoResultItem()
        assertEquals("", video.key)
        assertEquals("", video.name)
    }
}
