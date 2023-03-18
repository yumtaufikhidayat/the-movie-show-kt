package com.taufik.themovieshow.model.response.movie.detail


import com.google.gson.annotations.SerializedName

data class MovieDetailProductionCompany(
    @SerializedName("id")
    val id: Int,
    @SerializedName("logo_path")
    val logoPath: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("origin_country")
    val originCountry: String
)