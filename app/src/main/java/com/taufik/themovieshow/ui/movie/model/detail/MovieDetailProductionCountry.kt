package com.taufik.themovieshow.ui.movie.model.detail


import com.google.gson.annotations.SerializedName

data class MovieDetailProductionCountry(
    @SerializedName("iso_3166_1")
    val iso31661: String,
    @SerializedName("name")
    val name: String
)