package com.taufik.themovieshow.utils

import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.taufik.themovieshow.R
import com.taufik.themovieshow.api.UrlEndpoint
import kotlin.math.roundToInt

fun ImageView.loadImage(url: String) {
    Glide.with(this.context)
        .load(UrlEndpoint.IMAGE_URL + url)
        .apply(RequestOptions.placeholderOf(R.drawable.ic_loading).error(R.drawable.ic_error))
        .into(this)
}

fun toRating(data: Double): String {
    return ((data * 10.0).roundToInt() / 10.0).toString()
}