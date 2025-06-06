package com

import android.app.Application
import android.content.Context
import com.taufik.themovieshow.utils.language.ContextUtils
import com.taufik.themovieshow.utils.language.LANGUAGE
import com.taufik.themovieshow.utils.language.LanguageCache
import dagger.hilt.android.HiltAndroidApp
import java.util.Locale

@HiltAndroidApp
class App : Application() {
    override fun attachBaseContext(base: Context) {
        val langCode = base.getSharedPreferences(LanguageCache.LANGUAGE_PREF_CACHE, MODE_PRIVATE)
            .getString(LanguageCache.KEY_LANGUAGE, LANGUAGE.ENGLISH.code) ?: LANGUAGE.ENGLISH.code

        val locale = Locale(langCode)
        val newBase = ContextUtils.updateLocale(base, locale)
        super.attachBaseContext(newBase)
    }
}