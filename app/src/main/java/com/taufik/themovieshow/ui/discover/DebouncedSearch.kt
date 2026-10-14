package com.taufik.themovieshow.ui.discover

import com.taufik.themovieshow.data.NetworkResult
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.merge
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn

@OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
class DebouncedSearch<R, T>(
    scope: CoroutineScope,
    private val search: (String) -> Flow<NetworkResult<R>>,
    private val items: (R) -> List<T>,
    private val onError: (String) -> Unit,
    private val onSearch: (String) -> Unit = {},
    debounceMillis: Long = DEBOUNCE_MILLIS
) {

    private val typedQuery = MutableStateFlow("")
    private val submittedQuery = MutableSharedFlow<String>(extraBufferCapacity = 1)

    // Query that is in flight or already loaded. Cleared on error or cancellation, so it can be sent again.
    private var activeQuery: String? = null

    // Last query reported to onSearch, so a retry or a resend after resume is not reported again.
    private var reportedQuery: String? = null

    val state: StateFlow<SearchUiState<T>> = merge(
        typedQuery.debounce { if (it.isBlank()) 0L else debounceMillis },
        submittedQuery
    )
        .map { it.trim() }
        .filter { it != activeQuery }
        .flatMapLatest { query ->
            activeQuery = query
            if (query.isEmpty()) {
                reportedQuery = null
                flowOf(SearchUiState.Idle)
            } else {
                if (query != reportedQuery) {
                    reportedQuery = query
                    onSearch(query)
                }
                search(query)
                    .map { it.toUiState() }
                    .onEach {
                        if (it is SearchUiState.Error) {
                            activeQuery = null
                            onError(it.message)
                        }
                    }
                    .onCompletion { cause -> if (cause != null) activeQuery = null }
            }
        }
        .stateIn(
            scope = scope,
            started = SharingStarted.WhileSubscribed(STOP_TIMEOUT_MILLIS),
            initialValue = SearchUiState.Idle
        )

    fun onQueryChanged(query: String) {
        typedQuery.value = query
    }

    /** Skips the debounce. Also used to retry after an error. */
    fun searchNow() {
        submittedQuery.tryEmit(typedQuery.value)
    }

    private fun NetworkResult<R>.toUiState(): SearchUiState<T> = when (this) {
        is NetworkResult.Loading -> SearchUiState.Loading
        is NetworkResult.Error -> SearchUiState.Error(message.orEmpty())
        is NetworkResult.Success -> {
            val results = data?.let(items).orEmpty()
            if (results.isEmpty()) SearchUiState.Empty else SearchUiState.Success(results)
        }
    }

    companion object {
        const val DEBOUNCE_MILLIS = 500L
        const val STOP_TIMEOUT_MILLIS = 5000L
    }
}
