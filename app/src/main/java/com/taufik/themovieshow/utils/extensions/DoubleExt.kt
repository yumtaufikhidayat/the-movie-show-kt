package com.taufik.themovieshow.utils.extensions

import kotlin.math.roundToInt

fun Double.toRating(): String {
    val tenDouble = 10.0
    return ((this * tenDouble).roundToInt() / tenDouble).toString()
}