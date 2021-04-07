package com.taufik.themovieshow.ui.main.movie.data.nowplayingdetail


import com.google.gson.annotations.SerializedName

data class MovieNowPlayingDetailBelongsToCollection(
    @SerializedName("backdrop_path")
    val backdropPath: String,
    @SerializedName("id")
    val id: Int,
    @SerializedName("name")
    val name: String,
    @SerializedName("poster_path")
    val posterPath: String
)