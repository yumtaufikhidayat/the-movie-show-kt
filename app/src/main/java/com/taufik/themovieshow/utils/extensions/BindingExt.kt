package com.taufik.themovieshow.utils.extensions

import androidx.core.view.isVisible
import com.taufik.themovieshow.databinding.LayoutErrorBinding

fun LayoutErrorBinding.showError(message: String?) {
    root.isVisible = true
    tvErrorDesc.text = message
}