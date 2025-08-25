package com.taufik.themovieshow.ui.about.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.taufik.themovieshow.data.repository.TheMovieShowRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AboutViewModel @Inject constructor(private val repository: TheMovieShowRepository): ViewModel() {
     val languageFlow: Flow<String> = repository.languageFlow

    fun getAboutData(context: Context) = repository.getAboutData(context)

    fun setLanguage(code: String, isChanged: Boolean) {
        viewModelScope.launch {
            repository.setLanguage(code, isChanged)
        }
    }

    suspend fun getCurrentLanguage(): String = repository.getLanguage()
}