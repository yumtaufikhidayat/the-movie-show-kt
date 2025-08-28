package com.taufik.themovieshow.data.local.preferences.language

import kotlinx.coroutines.flow.Flow

interface ILanguagePreference {
    val languageFlow: Flow<String>
    suspend fun setLanguage(language: String, isChanged: Boolean)
    suspend fun getLanguage(): String
    fun getLanguageChangedMessage(): Flow<Boolean>
    suspend fun setLanguageChangedMessage(isChanged: Boolean)
}