package com.taufik.themovieshow.ui.main.movie.data.nowplaying


import com.google.gson.annotations.SerializedName

data class MovieNowPlayingDates(
    @SerializedName("maximum")
    val maximum: String,
    @SerializedName("minimum")
    val minimum: String
)