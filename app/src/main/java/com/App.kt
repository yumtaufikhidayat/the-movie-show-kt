package com

import android.app.Application
import android.content.Context
import com.taufik.themovieshow.utils.extensions.encryptAndStorePassphrase
import com.taufik.themovieshow.utils.extensions.secureDataStore
import com.taufik.themovieshow.utils.language.ContextUtils
import com.taufik.themovieshow.utils.language.LANGUAGE
import com.taufik.themovieshow.utils.language.LanguageCache
import com.taufik.themovieshow.utils.objects.PreferencesKey.PASSPHRASE
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import java.util.Locale
import java.util.UUID

@HiltAndroidApp
class App : Application() {
    override fun attachBaseContext(base: Context) {
        val langCode = base.getSharedPreferences(LanguageCache.LANGUAGE_PREF_CACHE, MODE_PRIVATE)
            .getString(LanguageCache.KEY_LANGUAGE, LANGUAGE.ENGLISH.code) ?: LANGUAGE.ENGLISH.code

        val locale = Locale(langCode)
        val newBase = ContextUtils.updateLocale(base, locale)
        super.attachBaseContext(newBase)
    }

    override fun onCreate() {
        super.onCreate()
        System.loadLibrary("sqlcipher")

        runBlocking {
            val passphraseExists = secureDataStore.data.first()[PASSPHRASE] != null
            if (!passphraseExists) {
                val rawPassphrase = UUID.randomUUID().toString()
                encryptAndStorePassphrase(rawPassphrase)
            }
        }
    }
}