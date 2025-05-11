package com.taufik.themovieshow.data.local.preferences.language

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.taufik.themovieshow.utils.extensions.languageDataStore
import com.taufik.themovieshow.utils.language.LANGUAGE
import com.taufik.themovieshow.utils.language.LanguageCache
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LanguagePreference @Inject constructor(
    @ApplicationContext private val context: Context
) : ILanguagePreference {

    private val dataStore = context.languageDataStore

    override suspend fun setLanguage(language: String) {
        dataStore.edit { preferences ->
            preferences[LANGUAGE_KEY] = language
        }
    }

    override suspend fun getLanguage(): String {
        val preferences = dataStore.data.first()
        return preferences[LANGUAGE_KEY] ?: LANGUAGE.ENGLISH.code
    }

    override val languageFlow: Flow<String>
        get() = dataStore.data.map { preferences ->
            preferences[LANGUAGE_KEY] ?: LANGUAGE.ENGLISH.code
        }

    companion object {
        private val LANGUAGE_KEY = stringPreferencesKey(LanguageCache.KEY_LANGUAGE)
    }
}