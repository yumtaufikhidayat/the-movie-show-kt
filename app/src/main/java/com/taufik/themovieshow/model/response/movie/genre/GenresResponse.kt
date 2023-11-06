package com.taufik.themovieshow.model.response.movie.genre


import com.google.gson.annotations.SerializedName

data class GenresResponse(
    @SerializedName("genres")
    val genres: List<Genre> = listOf()
)