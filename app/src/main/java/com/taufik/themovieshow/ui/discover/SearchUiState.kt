package com.taufik.themovieshow.ui.discover

sealed interface SearchUiState<out T> {
    data object Idle : SearchUiState<Nothing>
    data object Loading : SearchUiState<Nothing>
    data class Success<T>(val items: List<T>) : SearchUiState<T>
    data object Empty : SearchUiState<Nothing>
    data class Error(val message: String) : SearchUiState<Nothing>
}
