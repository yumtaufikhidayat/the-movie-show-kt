package com.taufik.themovieshow.ui.discover

import com.taufik.themovieshow.data.NetworkResult
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class DebouncedSearchTest {

    private val requests = mutableListOf<String>()
    private val errors = mutableListOf<String>()
    private val reportedSearches = mutableListOf<String>()
    private val states = mutableListOf<SearchUiState<String>>()
    private val failingQueries = mutableSetOf<String>()
    private var collector: Job? = null

    private fun TestScope.createSearch(): DebouncedSearch<List<String>, String> =
        DebouncedSearch<List<String>, String>(
            scope = backgroundScope,
            search = { query ->
                flow {
                    requests += query
                    emit(NetworkResult.Loading())
                    delay(if (query == SLOW_QUERY) SLOW_RESPONSE_MILLIS else RESPONSE_MILLIS)
                    if (query in failingQueries) {
                        emit(NetworkResult.Error(ERROR_MESSAGE))
                    } else {
                        emit(NetworkResult.Success(if (query == NO_RESULT_QUERY) emptyList() else listOf("$query result")))
                    }
                }
            },
            items = { it },
            onError = { errors += it },
            onSearch = { reportedSearches += it }
        ).also { subscribe(it) }

    private fun TestScope.subscribe(search: DebouncedSearch<List<String>, String>) {
        collector = backgroundScope.launch { search.state.collect { states += it } }
        runCurrent()
    }

    // advanceUntilIdle() skips backgroundScope, where the search runs, so move the clock past debounce + response.
    private fun TestScope.settle() {
        advanceTimeBy(DebouncedSearch.DEBOUNCE_MILLIS + RESPONSE_MILLIS + 1)
        runCurrent()
    }

    private fun DebouncedSearch<List<String>, String>.type(query: String) {
        query.indices.forEach { onQueryChanged(query.substring(0, it + 1)) }
    }

    @Test
    fun `typing a query sends one request after the debounce`() = runTest {
        val search = createSearch()

        search.type("avatar")
        advanceTimeBy(DebouncedSearch.DEBOUNCE_MILLIS - 1)
        assertTrue(requests.isEmpty())

        settle()
        assertEquals(listOf("avatar"), requests)
        assertEquals(SearchUiState.Success(listOf("avatar result")), search.state.value)
    }

    @Test
    fun `loading is shown while the request is in flight`() = runTest {
        val search = createSearch()

        search.type("avatar")
        advanceTimeBy(DebouncedSearch.DEBOUNCE_MILLIS + 1)
        assertEquals(SearchUiState.Loading, search.state.value)
    }

    @Test
    fun `blank query is not sent and shows idle`() = runTest {
        val search = createSearch()

        search.onQueryChanged("   ")
        settle()

        assertTrue(requests.isEmpty())
        assertEquals(SearchUiState.Idle, search.state.value)
    }

    @Test
    fun `clearing the query shows idle without waiting for the debounce`() = runTest {
        val search = createSearch()
        search.type("avatar")
        settle()

        search.onQueryChanged("")
        runCurrent()

        assertEquals(SearchUiState.Idle, search.state.value)
        assertEquals(listOf("avatar"), requests)
    }

    @Test
    fun `search now skips the debounce and is not sent twice`() = runTest {
        val search = createSearch()

        search.type("avatar")
        search.searchNow()
        runCurrent()
        assertEquals(listOf("avatar"), requests)

        settle()
        assertEquals(listOf("avatar"), requests)
        assertEquals(SearchUiState.Success(listOf("avatar result")), search.state.value)
    }

    @Test
    fun `same query after trim is not sent again`() = runTest {
        val search = createSearch()
        search.type("avatar")
        settle()

        search.onQueryChanged("avatar ")
        settle()
        search.searchNow()
        settle()

        assertEquals(listOf("avatar"), requests)
    }

    @Test
    fun `new query cancels the stale one`() = runTest {
        val search = createSearch()
        search.type("avatar")
        advanceTimeBy(DebouncedSearch.DEBOUNCE_MILLIS + RESPONSE_MILLIS / 2)

        search.type("batman")
        search.searchNow()
        settle()

        assertEquals(listOf("avatar", "batman"), requests)
        assertFalse(SearchUiState.Success(listOf("avatar result")) in states)
        assertEquals(SearchUiState.Success(listOf("batman result")), search.state.value)
    }

    @Test
    fun `no results shows empty`() = runTest {
        val search = createSearch()

        search.type(NO_RESULT_QUERY)
        settle()

        assertEquals(SearchUiState.Empty, search.state.value)
    }

    @Test
    fun `error is shown and reported once, then retry sends the same query again`() = runTest {
        val search = createSearch()
        failingQueries += "avatar"

        search.type("avatar")
        settle()
        assertEquals(SearchUiState.Error(ERROR_MESSAGE), search.state.value)
        assertEquals(listOf(ERROR_MESSAGE), errors)

        failingQueries.clear()
        search.searchNow()
        settle()

        assertEquals(listOf("avatar", "avatar"), requests)
        assertEquals(SearchUiState.Success(listOf("avatar result")), search.state.value)
        assertEquals(listOf(ERROR_MESSAGE), errors)
        assertEquals(listOf("avatar"), reportedSearches)
    }

    @Test
    fun `resubscribing after the stop timeout does not send a loaded query again`() = runTest {
        val search = createSearch()
        search.type("avatar")
        settle()

        collector?.cancel()
        advanceTimeBy(DebouncedSearch.STOP_TIMEOUT_MILLIS + 1)
        subscribe(search)
        settle()

        assertEquals(listOf("avatar"), requests)
        assertEquals(SearchUiState.Success(listOf("avatar result")), search.state.value)
    }

    @Test
    fun `resubscribing after an interrupted request sends it again`() = runTest {
        val search = createSearch()
        search.type(SLOW_QUERY)
        advanceTimeBy(DebouncedSearch.DEBOUNCE_MILLIS + 1)

        collector?.cancel()
        advanceTimeBy(DebouncedSearch.STOP_TIMEOUT_MILLIS + 1)
        subscribe(search)
        advanceTimeBy(DebouncedSearch.DEBOUNCE_MILLIS + SLOW_RESPONSE_MILLIS + 1)
        runCurrent()

        assertEquals(listOf(SLOW_QUERY, SLOW_QUERY), requests)
        assertEquals(SearchUiState.Success(listOf("$SLOW_QUERY result")), search.state.value)
        assertEquals(listOf(SLOW_QUERY), reportedSearches)
    }

    @Test
    fun `each sent query is reported once with its trimmed text`() = runTest {
        val search = createSearch()

        search.type(" avatar ")
        settle()
        search.searchNow()
        settle()
        search.type("avatars")
        settle()

        assertEquals(listOf("avatar", "avatars"), reportedSearches)
    }

    @Test
    fun `blank query is not reported and the same query is reported again after clearing`() = runTest {
        val search = createSearch()

        search.type("avatar")
        settle()
        search.onQueryChanged("")
        settle()
        search.type("avatar")
        settle()

        assertEquals(listOf("avatar", "avatar"), reportedSearches)
    }

    private companion object {
        const val RESPONSE_MILLIS = 300L
        const val ERROR_MESSAGE = "Unable to resolve host"
        const val NO_RESULT_QUERY = "zzzz"
        const val SLOW_QUERY = "slow"
        const val SLOW_RESPONSE_MILLIS = 10_000L
    }
}
