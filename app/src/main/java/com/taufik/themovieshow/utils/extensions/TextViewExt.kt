package com.taufik.themovieshow.utils.extensions

import android.text.SpannableString
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.taufik.themovieshow.R

fun TextView.releaseInfo(date: String, status: String) {
    val parts = mutableListOf<Pair<String, Int>>()

    if (date.isNotEmpty()) {
        parts.add(date to ContextCompat.getColor(context, R.color.colorLightGrey))
    }

    if (status.isNotEmpty()) {
        val color = when (status.lowercase()) {
            "released" -> ContextCompat.getColor(context, android.R.color.holo_green_light)
            "upcoming" -> ContextCompat.getColor(context, android.R.color.holo_orange_light)
            "ended" -> ContextCompat.getColor(context, android.R.color.holo_red_light)
            "returning series" -> ContextCompat.getColor(context, android.R.color.holo_blue_light)
            else -> ContextCompat.getColor(context, R.color.teal_700)
        }
        parts.add(status to color)
    }

    val fullText = parts.joinToString("  •  ") { it.first }
    val spannable = SpannableString(fullText)

    var start = 0
    parts.forEachIndexed { index, (text, color) ->
        val end = start + text.length
        spannable.setSpan(
            ForegroundColorSpan(color),
            start,
            end,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        start = end
        if (index < parts.size - 1) {
            start += 5
        }
    }

    this.setText(spannable, TextView.BufferType.SPANNABLE)
}