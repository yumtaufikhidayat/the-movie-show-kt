package com.taufik.themovieshow.ui.favorite.viewmodel

import com.taufik.themovieshow.data.local.entity.movie.FavoriteMovieEntity
import com.taufik.themovieshow.data.repository.FakeTheMovieShowRepository
import com.taufik.themovieshow.model.favorite.SortFiltering
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class FavoriteMovieViewModelTest {

    private lateinit var repository: FakeTheMovieShowRepository
    private lateinit var viewModel: FavoriteMovieViewModel

    private val movie = FavoriteMovieEntity(1, "/a.jpg", "Avatar", "2009-12-18", 7.6)

    @Before
    fun setUp() {
        repository = FakeTheMovieShowRepository()
        viewModel = FavoriteMovieViewModel(repository)
    }

    private fun sqlFor(position: Int): String {
        viewModel.setFavoriteOrder(position)
        return repository.lastMovieRawQuery!!.value.sql
    }

    @Test
    fun `nothing is queried before an order is set`() = runTest {
        val emissions = mutableListOf<List<FavoriteMovieEntity>>()
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.favoriteMoviesFlow.collect { emissions += it }
        }

        assertTrue(emissions.isEmpty())
        assertNull(repository.lastMovieRawQuery)
    }

    @Test
    fun `setting an order emits favorite movies`() = runTest {
        repository.favoriteMovies += movie

        viewModel.setFavoriteOrder(0)

        assertEquals(listOf(movie), viewModel.favoriteMoviesFlow.first())
    }

    @Test
    fun `each position builds the matching sort query`() = runTest {
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.favoriteMoviesFlow.collect {}
        }

        assertEquals("SELECT * FROM favorite_movie ", sqlFor(0))
        assertEquals("SELECT * FROM favorite_movie ORDER BY title ASC", sqlFor(1))
        assertEquals("SELECT * FROM favorite_movie ORDER BY release_date DESC", sqlFor(2))
        assertEquals("SELECT * FROM favorite_movie ORDER BY rating DESC", sqlFor(3))
    }

    @Test
    fun `changing the order switches to the latest query`() = runTest {
        val source = MutableStateFlow(listOf(movie))
        repository.favoriteMovieListFlow = source
        val emissions = mutableListOf<List<FavoriteMovieEntity>>()
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.favoriteMoviesFlow.collect { emissions += it }
        }

        viewModel.setFavoriteOrder(1)
        viewModel.setFavoriteOrder(3)
        source.value = emptyList()

        assertEquals("SELECT * FROM favorite_movie ORDER BY rating DESC", repository.lastMovieRawQuery!!.value.sql)
        assertEquals(listOf(listOf(movie), listOf(movie), emptyList()), emissions)
    }

    @Test
    fun `sort options come from the repository`() {
        val options = listOf(SortFiltering(0, 1), SortFiltering(1, 2))
        repository.sortFilteringList = options

        assertEquals(options, viewModel.getSortFiltering())
    }
}
