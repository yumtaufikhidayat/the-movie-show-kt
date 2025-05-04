package com.taufik.themovieshow.utils.extensions

import android.view.View
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible

fun View.showView() {
    this.isVisible = true
}

fun View.hideView() {
    this.isVisible = false
}

fun View.applySystemBarInsets(
    applyLeft: Boolean = false,
    applyTop: Boolean = false,
    applyRight: Boolean = false,
    applyBottom: Boolean = true
) {
    ViewCompat.setOnApplyWindowInsetsListener(this) { view, insets ->
        val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
        view.setPadding(
            if (applyLeft) systemBars.left else view.paddingLeft,
            if (applyTop) systemBars.top else view.paddingTop,
            if (applyRight) systemBars.right else view.paddingRight,
            if (applyBottom) systemBars.bottom else view.paddingBottom
        )
        insets
    }
}