package com.taufik.themovieshow.utils.extensions

import android.view.View
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible

fun View.showView() {
    this.isVisible = true
}

fun View.hideView() {
    this.isVisible = false
}

fun View.toggleVisibilityIf(condition: Boolean, useInvisible: Boolean = false) {
    visibility = when {
        condition -> View.VISIBLE
        useInvisible -> View.INVISIBLE
        else -> View.GONE
    }
}


fun View.applySystemBarInsets(
    applyLeft: Boolean = false,
    applyTop: Boolean = false,
    applyRight: Boolean = false,
    applyBottom: Boolean = true,
    extraBottomTarget: View? = null
) {
    ViewCompat.setOnApplyWindowInsetsListener(this) { view, insets ->
        val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())

        view.setPadding(
            if (applyLeft) systemBars.left else view.paddingLeft,
            if (applyTop) systemBars.top else view.paddingTop,
            if (applyRight) systemBars.right else view.paddingRight,
            if (applyBottom) systemBars.bottom else view.paddingBottom
        )

        // Jika ada view tambahan untuk menerima padding bawah
        extraBottomTarget?.setPadding(
            extraBottomTarget.paddingLeft,
            extraBottomTarget.paddingTop,
            extraBottomTarget.paddingRight,
            systemBars.bottom
        )

        insets
    }
}

fun ViewGroup.MarginLayoutParams.applyMiddleMargins(
    position: Int,
    itemCount: Int,
    middle: Int,
    convertDpToPx: (Int) -> Int
) {
    val middlePx = convertDpToPx(middle)
    marginStart = if (position == 0) 0 else middlePx
    marginEnd = if (position == itemCount - 1) 0 else middlePx
}
