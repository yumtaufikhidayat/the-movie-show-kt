package com.taufik.themovieshow.ui.discover

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.taufik.themovieshow.data.repository.ITheMovieShowRepository
import com.taufik.themovieshow.model.response.movie.discover.DiscoverMovieResult
import com.taufik.themovieshow.utils.extensions.recordNetworkError
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class DiscoverMovieViewModel @Inject constructor(repository: ITheMovieShowRepository) : ViewModel() {

    private val search = DebouncedSearch(
        scope = viewModelScope,
        search = repository::getDiscoverMovie,
        items = { it.results.orEmpty() },
        onError = ::recordNetworkError
    )

    val searchState: StateFlow<SearchUiState<DiscoverMovieResult>> = search.state

    fun onQueryChanged(query: String) = search.onQueryChanged(query)

    fun searchNow() = search.searchNow()
}
