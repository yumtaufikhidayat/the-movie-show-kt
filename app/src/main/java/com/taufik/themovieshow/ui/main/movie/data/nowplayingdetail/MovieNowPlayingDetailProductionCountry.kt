package com.taufik.themovieshow.ui.main.movie.data.nowplayingdetail


import com.google.gson.annotations.SerializedName

data class MovieNowPlayingDetailProductionCountry(
    @SerializedName("iso_3166_1")
    val iso31661: String,
    @SerializedName("name")
    val name: String
)