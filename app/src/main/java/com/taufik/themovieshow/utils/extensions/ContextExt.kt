package com.taufik.themovieshow.utils.extensions

import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import com.taufik.themovieshow.R
import es.dmoral.toasty.Toasty
import androidx.core.net.toUri

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