package com.taufik.themovieshow.utils.extensions

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Typeface
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.taufik.themovieshow.R
import com.taufik.themovieshow.ui.language.bottomsheet.LanguageBottomSheetDialog.Companion.LANGUAGE_CHANGED
import es.dmoral.toasty.Toasty

val Context.languageDataStore: DataStore<Preferences> by preferencesDataStore(name = "settings.preferences_pb")

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

fun Context.createCustomTabView(@StringRes titleRes: Int, isSelected: Boolean): View {
    val view = LayoutInflater.from(this).inflate(R.layout.custom_tab, null)
    val tabText = view.findViewById<TextView>(R.id.tabText)

    tabText.text = getString(titleRes)
    tabText.setTypeface(null, if (isSelected) Typeface.BOLD else Typeface.NORMAL)
    tabText.setTextColor(
        ContextCompat.getColor(
            this,
            if (isSelected) android.R.color.white else R.color.colorSemiBlack
        )
    )

    tabText.background = if (isSelected) ContextCompat.getDrawable(this, R.drawable.bg_tab) else null
    return view
}

fun Context.restartAppWithLanguageChange(context: Context, activity: Class<out Activity>) {
    val intent = Intent(context, activity).apply {
        putExtra(LANGUAGE_CHANGED, true)
        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
    }

    context.startActivity(intent)

    if (context is Activity) {
        context.finish()
    }
}