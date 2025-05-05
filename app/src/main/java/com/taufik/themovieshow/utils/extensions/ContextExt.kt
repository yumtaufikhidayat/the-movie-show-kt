package com.taufik.themovieshow.utils.extensions

import android.content.Context
import android.content.Intent
import android.graphics.Typeface
import android.text.TextUtils
import android.util.Log
import android.util.TypedValue
import android.view.Gravity
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import com.taufik.themovieshow.R
import es.dmoral.toasty.Toasty

fun Context.showTrailerVideo(key: String) {
    try {
        startActivity(Intent(Intent.ACTION_VIEW, "vnd.youtube://$key".toUri()))
    } catch (e: Exception) {
        this.showSuccessToastyIcon(getString(R.string.tvInstallBrowserYouTube, e.localizedMessage))
    }
}

fun Context.share(shareText: String, link: String) {
    try {
        val body = StringBuilder(shareText).append(link).toString()
        val shareIntent = Intent(Intent.ACTION_SEND)
        shareIntent.apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT, body)
        }
        startActivity(
            Intent.createChooser(
                shareIntent,
                getString(R.string.tvShareWith)
            )
        )
    } catch (e: Exception) {
        Log.e("TAG", "Error: ${e.localizedMessage}")
        showErrorToastyIcon(getString(R.string.tvOops))
    }
}

fun Context.showSuccessToastyIcon(message: String) {
    Toasty.success(this, message, Toast.LENGTH_SHORT, true).show()
}

fun Context.showSuccessToasty(message: String) {
    Toasty.success(this, message, Toast.LENGTH_SHORT, false).show()
}

fun Context.showErrorToastyIcon(message: String) {
    Toasty.error(this, message, Toast.LENGTH_SHORT, true).show()
}

fun Context.createCustomTabView(@StringRes titleRes: Int, isSelected: Boolean): TextView {
    return TextView(this).apply {
        text = getString(titleRes)
        gravity = Gravity.CENTER
        maxLines = 1
        minWidth = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP, 80f, resources.displayMetrics
        ).toInt()
        ellipsize = TextUtils.TruncateAt.END
        isSingleLine = true
        setPadding(17, 0, 17, 0)
        setTextSize(TypedValue.COMPLEX_UNIT_SP, 14f)
        setTypeface(null, if (isSelected) Typeface.BOLD else Typeface.NORMAL)
        setTextColor(
            ContextCompat.getColor(
                this@createCustomTabView,
                if (isSelected) R.color.white else R.color.colorSemiBlack
            )
        )
    }
}