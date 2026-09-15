package com.taufik.themovieshow.ui.discover

import com.taufik.themovieshow.data.NetworkResult
import com.taufik.themovieshow.data.repository.FakeTheMovieShowRepository
import com.taufik.themovieshow.model.response.movie.discover.DiscoverMovieResponse
import com.taufik.themovieshow.model.response.movie.discover.DiscoverMovieResult
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
class DiscoverMovieViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule(StandardTestDispatcher())

    private lateinit var repository: FakeTheMovieShowRepository
    private lateinit var viewModel: DiscoverMovieViewModel
    private val requests = mutableListOf<String>()

    private val avatar = DiscoverMovieResult(
        adult = false, backdropPath = null, genreIds = null, id = 19995,
        originalLanguage = "en", originalTitle = "Avatar", overview = null, popularity = 1.0,
        posterPath = null, releaseDate = "2009-12-18", title = "Avatar", video = false,
        voteAverage = 7.6, voteCount = 100
    )

    @Before
    fun setUp() {
        repository = FakeTheMovieShowRepository()
        repository.discoverMovieHandler = { query ->
            flow {
                requests += query
                emit(NetworkResult.Loading())
                val results = if (query == "avatar") listOf(avatar) else emptyList()
                emit(NetworkResult.Success(DiscoverMovieResponse(1, results, 1, results.size)))
            }
        }
        viewModel = DiscoverMovieViewModel(repository)
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

        "avatar".indices.forEach { viewModel.onQueryChanged("avatar".substring(0, it + 1)) }
        settle()

        assertEquals(listOf("avatar"), requests)
        assertEquals(SearchUiState.Success(listOf(avatar)), viewModel.searchState.value)
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

        viewModel.onQueryChanged("avatar")
        viewModel.searchNow()
        runCurrent()

        assertEquals(listOf("avatar"), requests)
        assertEquals(SearchUiState.Success(listOf(avatar)), viewModel.searchState.value)
    }
}
