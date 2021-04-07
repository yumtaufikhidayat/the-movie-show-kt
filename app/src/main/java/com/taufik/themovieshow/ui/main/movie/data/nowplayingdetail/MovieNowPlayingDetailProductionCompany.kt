package com.taufik.themovieshow.ui.main.movie.data.nowplayingdetail


import com.google.gson.annotations.SerializedName

data class MovieNowPlayingDetailProductionCompany(
    @SerializedName("id")
    val id: Int,
    @SerializedName("logo_path")
    val logoPath: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("origin_country")
    val originCountry: String
)