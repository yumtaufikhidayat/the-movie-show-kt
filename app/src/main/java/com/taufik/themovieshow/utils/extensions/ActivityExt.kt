package com.taufik.themovieshow.utils.extensions

import android.app.Activity
import android.content.Intent
import com.taufik.themovieshow.ui.language.bottomsheet.LanguageBottomSheetDialog.Companion.SUCCESS_CHANGE_LANGUAGE

fun Activity.refreshActivity(withSuccess: Boolean = false) {
    val newIntent = Intent(this, this::class.java).apply {
        if (withSuccess) {
            putExtra(SUCCESS_CHANGE_LANGUAGE, true)
        }
        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
    }
    startActivity(newIntent)
    finish()
    overridePendingTransition(0, 0)
}
