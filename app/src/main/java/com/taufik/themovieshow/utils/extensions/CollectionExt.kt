package com.taufik.themovieshow.utils.extensions

import android.content.Context
import com.taufik.themovieshow.R

fun Collection<*>.orNA(context: Context): String = if (this.isEmpty()) context.getString(R.string.tvNA) else this.joinToString()