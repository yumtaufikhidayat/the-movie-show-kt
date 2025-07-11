package com.taufik.themovieshow.utils.extensions

import android.content.Context
import com.taufik.themovieshow.R
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun String.convertDate(inputFormat: String, outputFormat: String): String {
    val formatter = SimpleDateFormat(inputFormat, Locale.US)
    var formatParser = Date()
    if (this.isNotEmpty()) {
        formatParser = formatter.parse(this) ?: Date()
    }
    val newOutputFormat = SimpleDateFormat(outputFormat, Locale.US)
    return newOutputFormat.format(formatParser)
}

fun String.orNA(context: Context): String = this.ifBlank { context.getString(R.string.tvNA) }