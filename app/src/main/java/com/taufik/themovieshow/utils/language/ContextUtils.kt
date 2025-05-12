package com.taufik.themovieshow.utils.language

import android.content.Context
import android.content.ContextWrapper
import android.content.res.Configuration
import android.os.Build
import android.os.LocaleList
import java.util.Locale

class ContextUtils(base: Context) : ContextWrapper(base) {
    companion object {
        fun updateLocale(c: Context, newLocale: Locale): ContextWrapper {
            Locale.setDefault(newLocale)
            val config = Configuration(c.resources.configuration)

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                config.setLocale(newLocale)
                config.setLocales(LocaleList(newLocale))
            } else {
                config.setLocale(newLocale)
            }

            // 👇 Tambahan krusial: update default resource configuration
            @Suppress("DEPRECATION")
            c.resources.updateConfiguration(config, c.resources.displayMetrics)

            val localizedContext = c.createConfigurationContext(config)
            return ContextUtils(localizedContext)
        }
    }
}