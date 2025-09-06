package com.taufik.themovieshow.utils.extensions

import android.app.Activity
import android.content.Intent

fun Activity.refreshActivity() {
    val newIntent = Intent(this, this::class.java).apply {
        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
    }
    startActivity(newIntent)
    finish()
    overridePendingTransition(0, 0)
}
