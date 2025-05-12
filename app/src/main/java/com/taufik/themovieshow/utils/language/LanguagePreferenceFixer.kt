package com.taufik.themovieshow.utils.language

import android.content.Context
import android.util.Log
import java.io.File

object LanguagePreferenceFixer {
    private const val INVALID_FILE_NAME = "settings"
    private const val VALID_FILE_NAME = "settings.preferences_pb"

    /**
     * Safely remove old DataStore file if it doesn't match required extension.
     * Call this once on app start (e.g., from Application or BaseActivity).
     */
    fun fixIfInvalid(context: Context) {
        val dir = File(context.filesDir, "datastore")
        val invalidFile = File(dir, INVALID_FILE_NAME)
        val validFile = File(dir, VALID_FILE_NAME)

        // If "settings" exists but valid .preferences_pb does not, remove the invalid one
        if (invalidFile.exists() && !validFile.exists()) {
            val deleted = invalidFile.delete()
            Log.w("LanguageFixer", "Deleted invalid DataStore file: $deleted")
        }
    }
}