package com.taufik.themovieshow.utils.extensions

import android.view.View
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import androidx.core.view.updatePadding

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
    applyBottom: Boolean = true,
    extraBottomTarget: View? = null // optional target tambahan seperti Guideline atau dummy view
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

fun View.applySystemBarBottomPadding() {
    ViewCompat.setOnApplyWindowInsetsListener(this) { view, insets ->
        val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
        view.setPadding(
            view.paddingLeft,
            view.paddingTop,
            view.paddingRight,
            systemBars.bottom
        )
        insets
    }
}