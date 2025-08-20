package com.taufik.themovieshow.utils.extensions

import android.app.Activity

fun Activity.refreshActivity() {
    val intent = intent
    finish()
    overridePendingTransition(0, 0)
    startActivity(intent)
    overridePendingTransition(0, 0)
}
