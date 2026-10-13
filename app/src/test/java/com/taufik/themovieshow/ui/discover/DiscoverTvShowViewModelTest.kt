package com.taufik.themovieshow.ui.discover

import com.taufik.themovieshow.data.NetworkResult
import com.taufik.themovieshow.data.repository.FakeTheMovieShowRepository
import com.taufik.themovieshow.model.response.tvshow.discover.DiscoverTvShowsResponse
import com.taufik.themovieshow.model.response.tvshow.discover.DiscoverTvShowsResult
import com.taufik.themovieshow.utils.MainDispatcherRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class DiscoverTvShowViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule(StandardTestDispatcher())

    private lateinit var repository: FakeTheMovieShowRepository
    private lateinit var viewModel: DiscoverTvShowViewModel
    private val requests = mutableListOf<String>()

    private val shogun = DiscoverTvShowsResult(
        backdropPath = null, firstAirDate = "2024-02-27", genreIds = null, id = 126308,
        name = "Shogun", originCountry = null, originalLanguage = "en", originalName = "Shogun",
        overview = null, popularity = 1.0, posterPath = null, voteAverage = 8.6, voteCount = 100
    )

    @Before
    fun setUp() {
        repository = FakeTheMovieShowRepository()
        repository.discoverTvShowsHandler = { query ->
            flow {
                requests += query
                emit(NetworkResult.Loading())
                val results = if (query == "shogun") listOf(shogun) else emptyList()
                emit(NetworkResult.Success(DiscoverTvShowsResponse(1, results, 1, results.size)))
            }
        }
        viewModel = DiscoverTvShowViewModel(repository)
    }

    private fun TestScope.subscribe() {
        backgroundScope.launch { viewModel.searchState.collect {} }
        runCurrent()
    }

    private fun TestScope.settle() {
        advanceTimeBy(DebouncedSearch.DEBOUNCE_MILLIS + 1)
        runCurrent()
    }

    @Test
    fun `initial state is idle`() = runTest {
        subscribe()

        assertEquals(SearchUiState.Idle, viewModel.searchState.value)
        assertTrue(requests.isEmpty())
    }

    @Test
    fun `typed query is debounced into one request with mapped results`() = runTest {
        subscribe()

        "shogun".indices.forEach { viewModel.onQueryChanged("shogun".substring(0, it + 1)) }
        settle()

        assertEquals(listOf("shogun"), requests)
        assertEquals(SearchUiState.Success(listOf(shogun)), viewModel.searchState.value)
    }

    @Test
    fun `query without results shows empty state`() = runTest {
        subscribe()

        viewModel.onQueryChanged("zzzz")
        settle()

        assertEquals(SearchUiState.Empty, viewModel.searchState.value)
    }

    @Test
    fun `searchNow skips the debounce`() = runTest {
        subscribe()

        viewModel.onQueryChanged("shogun")
        viewModel.searchNow()
        runCurrent()

        assertEquals(listOf("shogun"), requests)
        assertEquals(SearchUiState.Success(listOf(shogun)), viewModel.searchState.value)
    }
}
