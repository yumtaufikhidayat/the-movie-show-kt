package com.taufik.themovieshow.model.response.movie.cast


import com.google.gson.annotations.SerializedName

data class MovieCastResponse(
    @SerializedName("cast")
    val cast: List<com.taufik.themovieshow.model.response.movie.cast.MovieCast>,
    @SerializedName("crew")
    val crew: List<com.taufik.themovieshow.model.response.movie.cast.MovieCastCrew>,
    @SerializedName("id")
    val id: Int
)