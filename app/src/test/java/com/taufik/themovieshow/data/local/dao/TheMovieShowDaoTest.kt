package com.taufik.themovieshow.data.local.dao

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.taufik.themovieshow.data.local.entity.movie.FavoriteMovieEntity
import com.taufik.themovieshow.data.local.entity.tvshow.FavoriteTvShowEntity
import com.taufik.themovieshow.data.local.room.TheMovieShowDatabase
import com.taufik.themovieshow.data.source.RawQuery
import com.taufik.themovieshow.utils.objects.CommonConstants
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class TheMovieShowDaoTest {

    private lateinit var database: TheMovieShowDatabase
    private lateinit var dao: TheMovieShowDao

    private val movieA = FavoriteMovieEntity(1, "/a.jpg", "Avatar", "2009-12-18", 7.6)
    private val movieB = FavoriteMovieEntity(2, "/b.jpg", "Batman", "2022-03-04", 7.7)
    private val movieC = FavoriteMovieEntity(3, "/c.jpg", "Cars", "2006-06-09", 7.1)

    private val tvA = FavoriteTvShowEntity(10, "/a.jpg", "Arcane", "2021-11-06", 8.7)
    private val tvB = FavoriteTvShowEntity(20, "/b.jpg", "Breaking Bad", "2008-01-20", 8.9)

    @Before
    fun setUp() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        database = Room.inMemoryDatabaseBuilder(context, TheMovieShowDatabase::class.java)
            .allowMainThreadQueries()
            .build()
        dao = database.getFavoriteTheMovieShowDao()
    }

    @After
    fun tearDown() {
        database.close()
    }

    private fun movieQuery(column: String? = null, isAscending: Boolean = true) =
        RawQuery.Companion.Builder()
            .selectAll()
            .from(CommonConstants.TABLE_NAME_FAVORITE_MOVIE_ENTITY)
            .let { if (column != null) it.orderBy(column, isAscending) else it }
            .build()
            .value

    private fun tvShowQuery(column: String? = null, isAscending: Boolean = true) =
        RawQuery.Companion.Builder()
            .selectAll()
            .from(CommonConstants.TABLE_NAME_FAVORITE_TV_SHOW_ENTITY)
            .let { if (column != null) it.orderBy(column, isAscending) else it }
            .build()
            .value

    @Test
    fun `added movie is marked as favorite`() = runTest {
        dao.addMovieToFavorite(movieA)

        assertEquals(1, dao.checkMovieFavorite(movieA.movieId))
        assertEquals(0, dao.checkMovieFavorite(movieB.movieId))
    }

    @Test
    fun `adding the same movie twice replaces it`() = runTest {
        dao.addMovieToFavorite(movieA)
        dao.addMovieToFavorite(movieA.copy(movieTitle = "Avatar (Updated)"))

        val movies = dao.getFavoriteMovie(movieQuery()).first()
        assertEquals(listOf(movieA.copy(movieTitle = "Avatar (Updated)")), movies)
    }

    @Test
    fun `removing a movie deletes only that movie`() = runTest {
        dao.addMovieToFavorite(movieA)
        dao.addMovieToFavorite(movieB)

        assertEquals(1, dao.removeMovieFromFavorite(movieA.movieId))
        assertEquals(0, dao.removeMovieFromFavorite(movieA.movieId))
        assertEquals(listOf(movieB), dao.getFavoriteMovie(movieQuery()).first())
    }

    @Test
    fun `favorite movies are sorted by the raw query`() = runTest {
        listOf(movieB, movieC, movieA).forEach { dao.addMovieToFavorite(it) }

        assertEquals(
            listOf(movieA, movieB, movieC),
            dao.getFavoriteMovie(movieQuery(CommonConstants.COLUMN_NAME_TITLE, true)).first()
        )
        assertEquals(
            listOf(movieB, movieA, movieC),
            dao.getFavoriteMovie(movieQuery(CommonConstants.COLUMN_NAME_RELEASE_DATE, false)).first()
        )
        assertEquals(
            listOf(movieB, movieA, movieC),
            dao.getFavoriteMovie(movieQuery(CommonConstants.COLUMN_NAME_RATING, false)).first()
        )
    }

    @Test
    fun `empty movie table returns an empty list`() = runTest {
        assertTrue(dao.getFavoriteMovie(movieQuery()).first().isEmpty())
    }

    @Test
    fun `added tv show is marked as favorite`() = runTest {
        dao.addTvShowToFavorite(tvA)

        assertEquals(1, dao.checkTvShowFavorite(tvA.tvShowId))
        assertEquals(0, dao.checkTvShowFavorite(tvB.tvShowId))
    }

    @Test
    fun `removing a tv show deletes only that tv show`() = runTest {
        dao.addTvShowToFavorite(tvA)
        dao.addTvShowToFavorite(tvB)

        assertEquals(1, dao.removeTvShowFromFavorite(tvA.tvShowId))
        assertEquals(listOf(tvB), dao.getFavoriteTvShows(tvShowQuery()).first())
    }

    @Test
    fun `favorite tv shows are sorted by the raw query`() = runTest {
        dao.addTvShowToFavorite(tvB)
        dao.addTvShowToFavorite(tvA)

        assertEquals(
            listOf(tvA, tvB),
            dao.getFavoriteTvShows(tvShowQuery(CommonConstants.COLUMN_NAME_TITLE, true)).first()
        )
        assertEquals(
            listOf(tvA, tvB),
            dao.getFavoriteTvShows(tvShowQuery(CommonConstants.COLUMN_NAME_FIRST_AIR_DATE, false)).first()
        )
        assertEquals(
            listOf(tvB, tvA),
            dao.getFavoriteTvShows(tvShowQuery(CommonConstants.COLUMN_NAME_RATING, false)).first()
        )
    }

    @Test
    fun `movie and tv show tables are independent`() = runTest {
        dao.addMovieToFavorite(movieA)

        assertTrue(dao.getFavoriteTvShows(tvShowQuery()).first().isEmpty())
        assertEquals(0, dao.checkTvShowFavorite(movieA.movieId))
    }
}
