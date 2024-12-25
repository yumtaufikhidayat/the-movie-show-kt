package com.taufik.themovieshow.utils.extensions

import android.view.View
import androidx.core.view.isVisible

fun View.showView() {
    this.isVisible = true
}

fun View.hideView() {
    this.isVisible = false
}