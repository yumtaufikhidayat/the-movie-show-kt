package com.taufik.themovieshow.utils.extensions

import android.text.SpannableString
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.taufik.themovieshow.R

fun TextView.stringFormat(label: String, value: String) {
    this.text = String.format(
        "%s %s",
        label,
        value
    )
}

fun TextView.stringReleaseFormat(label: String, value: String) {
    val fullText = "$label $value"
    val spannable = SpannableString(fullText)

    spannable.setSpan(
        ForegroundColorSpan(ContextCompat.getColor(context, R.color.colorGrey)),
        0,
        label.length,
        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
    )

    // Warna value → hijau
    spannable.setSpan(
        ForegroundColorSpan(ContextCompat.getColor(context, android.R.color.holo_green_light)),
        label.length + 1,
        fullText.length,
        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
    )

    this.setText(spannable, TextView.BufferType.SPANNABLE)
}