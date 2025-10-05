package com.taufik.themovieshow.utils.objects

import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import com.taufik.themovieshow.utils.language.LanguageCache
import com.taufik.themovieshow.utils.objects.CommonConstants.KEY_IV
import com.taufik.themovieshow.utils.objects.CommonConstants.KEY_LAST_PASSPHRASE_HASH
import com.taufik.themovieshow.utils.objects.CommonConstants.KEY_PASSPHRASE

object PreferencesKey {
    val LANGUAGE_KEY = stringPreferencesKey(LanguageCache.KEY_LANGUAGE)
    val LANGUAGE_CHANGED_MESSAGE_KEY = booleanPreferencesKey(LanguageCache.KEY_MESSAGE_LANGUAGE_CHANGED)
    val IV_KEY = stringPreferencesKey(KEY_IV)
    val PASSPHRASE_KEY = stringPreferencesKey(KEY_PASSPHRASE)
    val PASS_HASH_KEY = stringPreferencesKey(KEY_LAST_PASSPHRASE_HASH)
}