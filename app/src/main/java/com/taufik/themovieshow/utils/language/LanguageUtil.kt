package com.taufik.themovieshow.utils.language

import android.content.Context
import java.util.Locale

object LanguageUtil {
    fun getCurrentLocaleBlocking(context: Context): Locale {
        val langCode = LanguageCache.get(context)
        return Locale(langCode)
    }
}