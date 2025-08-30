package com.taufik.themovieshow.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.taufik.themovieshow.data.repository.TheMovieShowRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val repository: TheMovieShowRepository): ViewModel() {

    val languageChangedMessage: Flow<Boolean> = repository.getLanguageChangedMessage()

    fun clearLanguageChangedFlag() {
        viewModelScope.launch {
            repository.setLanguageChangedMessage(false)
        }
    }
}