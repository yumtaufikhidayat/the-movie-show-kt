package com.taufik.themovieshow.ui.movie.model.cast


import com.google.gson.annotations.SerializedName

data class MovieCastResponse(
    @SerializedName("cast")
    val cast: List<MovieCast>,
    @SerializedName("crew")
    val crew: List<MovieCastCrew>,
    @SerializedName("id")
    val id: Int
)