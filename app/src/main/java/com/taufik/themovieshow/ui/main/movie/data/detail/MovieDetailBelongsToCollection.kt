package com.taufik.themovieshow.ui.main.movie.data.detail


import com.google.gson.annotations.SerializedName

data class MovieDetailBelongsToCollection(
    @SerializedName("backdrop_path")
    val backdropPath: String,
    @SerializedName("id")
    val id: Int,
    @SerializedName("name")
    val name: String,
    @SerializedName("poster_path")
    val posterPath: String
)