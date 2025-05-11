package com.taufik.themovieshow.utils.language

import android.content.Context
import androidx.core.content.edit

object LanguageCache {
    internal const val LANGUAGE_PREF_CACHE = "language_pref_cache"
    internal const val KEY_LANGUAGE = "app_language"

    fun save(context: Context, lang: String) {
        context.getSharedPreferences(LANGUAGE_PREF_CACHE, Context.MODE_PRIVATE)
            .edit {
                putString(KEY_LANGUAGE, lang)
            }
    }

    fun get(context: Context): String {
        return context.getSharedPreferences(LANGUAGE_PREF_CACHE, Context.MODE_PRIVATE)
            .getString(KEY_LANGUAGE, LANGUAGE.ENGLISH.code) ?: LANGUAGE.ENGLISH.code
    }
}