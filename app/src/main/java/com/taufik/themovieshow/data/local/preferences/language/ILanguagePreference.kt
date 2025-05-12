package com.taufik.themovieshow.data.local.preferences.language

import kotlinx.coroutines.flow.Flow

interface ILanguagePreference {
    suspend fun setLanguage(language: String)
    suspend fun getLanguage(): String
    val languageFlow: Flow<String>
}