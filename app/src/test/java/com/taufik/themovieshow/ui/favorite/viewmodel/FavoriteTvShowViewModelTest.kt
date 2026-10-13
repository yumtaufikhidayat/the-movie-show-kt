package com.taufik.themovieshow.ui.favorite.viewmodel

import com.taufik.themovieshow.data.local.entity.tvshow.FavoriteTvShowEntity
import com.taufik.themovieshow.data.repository.FakeTheMovieShowRepository
import com.taufik.themovieshow.model.favorite.SortFiltering
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class FavoriteTvShowViewModelTest {

    private lateinit var repository: FakeTheMovieShowRepository
    private lateinit var viewModel: FavoriteTvShowViewModel

    private val tvShow = FavoriteTvShowEntity(10, "/a.jpg", "Arcane", "2021-11-06", 8.7)

    @Before
    fun setUp() {
        repository = FakeTheMovieShowRepository()
        viewModel = FavoriteTvShowViewModel(repository)
    }

    private fun sqlFor(position: Int): String {
        viewModel.setFavoriteOrder(position)
        return repository.lastTvShowRawQuery!!.value.sql
    }

    @Test
    fun `nothing is queried before an order is set`() = runTest {
        val emissions = mutableListOf<List<FavoriteTvShowEntity>>()
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.favoriteTvShowsFlow.collect { emissions += it }
        }

        assertTrue(emissions.isEmpty())
        assertNull(repository.lastTvShowRawQuery)
    }

    @Test
    fun `setting an order emits favorite tv shows`() = runTest {
        repository.favoriteTvShows += tvShow

        viewModel.setFavoriteOrder(0)

        assertEquals(listOf(tvShow), viewModel.favoriteTvShowsFlow.first())
    }

    @Test
    fun `each position builds the matching sort query`() = runTest {
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.favoriteTvShowsFlow.collect {}
        }

        assertEquals("SELECT * FROM favorite_tv_show ", sqlFor(0))
        assertEquals("SELECT * FROM favorite_tv_show ORDER BY title ASC", sqlFor(1))
        assertEquals("SELECT * FROM favorite_tv_show ORDER BY first_air_date DESC", sqlFor(2))
        assertEquals("SELECT * FROM favorite_tv_show ORDER BY rating DESC", sqlFor(3))
    }

    @Test
    fun `sort options come from the repository`() {
        val options = listOf(SortFiltering(0, 1), SortFiltering(1, 2))
        repository.sortFilteringList = options

        assertEquals(options, viewModel.getSortFiltering())
    }
}
