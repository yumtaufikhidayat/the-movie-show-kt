package com.taufik.themovieshow.data.main.movie.detail


import com.google.gson.annotations.SerializedName

data class MovieDetailGenre(
    @SerializedName("id")
    val id: Int,
    @SerializedName("name")
    val name: String
)