package com.taufik.themovieshow.utils.extensions

import android.app.Activity
import com.taufik.themovieshow.ui.language.bottomsheet.LanguageBottomSheetDialog.Companion.SUCCESS_CHANGE_LANGUAGE

fun Activity.refreshActivity(withSuccess: Boolean = false) {
    val newIntent = intent.apply {
        if (withSuccess) {
            putExtra(SUCCESS_CHANGE_LANGUAGE, true)
        }
    }
    finish()
    overridePendingTransition(0, 0)
    startActivity(newIntent)
    overridePendingTransition(0, 0)
}
