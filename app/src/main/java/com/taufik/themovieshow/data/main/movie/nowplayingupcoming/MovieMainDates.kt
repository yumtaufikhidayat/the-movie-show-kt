package com.taufik.themovieshow.data.main.movie.nowplayingupcoming


import com.google.gson.annotations.SerializedName

data class MovieMainDates(
    @SerializedName("maximum")
    val maximum: String,
    @SerializedName("minimum")
    val minimum: String
)