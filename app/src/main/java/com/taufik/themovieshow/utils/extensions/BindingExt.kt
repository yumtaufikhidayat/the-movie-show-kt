package com.taufik.themovieshow.utils.extensions

import android.content.Context
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import com.taufik.themovieshow.R
import com.taufik.themovieshow.databinding.LayoutErrorBinding

fun LayoutErrorBinding.showError(message: String?) {
    root.isVisible = true
    tvErrorDesc.text = message
}

fun LayoutErrorBinding.showError(context: Context, message: String?, retryAction: (() -> Unit)? = null) {
    root.isVisible = true
    root.setBackgroundColor(
        ContextCompat.getColor(
            context,
            R.color.colorPrimaryDark
        )
    )
    tvErrorDesc.text = message
    btnRetry.apply {
        isVisible = retryAction != null
        setOnClickListener { retryAction?.invoke() }
    }
}