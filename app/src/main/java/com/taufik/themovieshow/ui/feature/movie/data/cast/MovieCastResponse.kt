package com.taufik.themovieshow.ui.feature.movie.data.cast


import com.google.gson.annotations.SerializedName

data class MovieCastResponse(
    @SerializedName("cast")
    val cast: List<MovieCast>,
    @SerializedName("crew")
    val crew: List<MovieCastCrew>,
    @SerializedName("id")
    val id: Int
)