package com.taufik.themovieshow.utils.extensions

import android.content.Context
import com.taufik.themovieshow.R
import java.text.SimpleDateFormat
import java.util.Locale

/**
 * Returns null when the string is blank or doesn't match [inputFormat],
 * so callers can fall back with `?: getString(R.string.tvNA)`.
 */
fun String.convertDate(inputFormat: String, outputFormat: String): String? =
    convertDate(listOf(inputFormat), outputFormat)

/**
 * Tries each of [inputFormats] in order and formats the first successful parse.
 * Returns null when the string is blank or matches none of them.
 */
fun String.convertDate(inputFormats: List<String>, outputFormat: String): String? {
    if (this.isBlank()) return null
    val date = inputFormats.firstNotNullOfOrNull { format ->
        runCatching { SimpleDateFormat(format, Locale.US).parse(this) }.getOrNull()
    } ?: return null
    return SimpleDateFormat(outputFormat, Locale.US).format(date)
}

fun String?.orNA(context: Context): String = if (this.isNullOrBlank()) context.getString(R.string.tvNA) else this
