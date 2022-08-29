package com.taufik.themovieshow.data.main.movie.cast


import com.google.gson.annotations.SerializedName

data class MovieCastResponse(
    @SerializedName("cast")
    val cast: List<MovieCast>,
    @SerializedName("crew")
    val crew: List<MovieCastCrew>,
    @SerializedName("id")
    val id: Int
)