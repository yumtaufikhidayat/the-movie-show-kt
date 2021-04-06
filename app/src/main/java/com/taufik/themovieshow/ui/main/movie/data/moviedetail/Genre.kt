package com.taufik.themovieshow.ui.main.movie.data.moviedetail


import com.google.gson.annotations.SerializedName

data class Genre(
    @SerializedName("id")
    val id: Int,
    @SerializedName("name")
    val name: String
)