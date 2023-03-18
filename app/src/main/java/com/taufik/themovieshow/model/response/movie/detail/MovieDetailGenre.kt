package com.taufik.themovieshow.model.response.movie.detail


import com.google.gson.annotations.SerializedName

data class MovieDetailGenre(
    @SerializedName("id")
    val id: Int,
    @SerializedName("name")
    val name: String
)