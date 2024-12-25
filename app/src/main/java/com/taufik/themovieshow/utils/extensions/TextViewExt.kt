package com.taufik.themovieshow.utils.extensions

import android.widget.TextView

fun TextView.stringFormat(data1: String, data2: String) {
    this.text = String.format(
        "%s %s",
        data1,
        data2
    )
}