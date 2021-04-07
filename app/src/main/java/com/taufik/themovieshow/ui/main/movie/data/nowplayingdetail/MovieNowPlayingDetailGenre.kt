package com.taufik.themovieshow.ui.main.movie.data.nowplayingdetail


import com.google.gson.annotations.SerializedName

data class MovieNowPlayingDetailGenre(
    @SerializedName("id")
    val id: Int,
    @SerializedName("name")
    val name: String
)