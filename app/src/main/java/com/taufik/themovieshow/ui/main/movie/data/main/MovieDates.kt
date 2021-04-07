package com.taufik.themovieshow.ui.main.movie.data.main


import com.google.gson.annotations.SerializedName

data class MovieDates(
    @SerializedName("maximum")
    val maximum: String,
    @SerializedName("minimum")
    val minimum: String
)