package com.taufik.themovieshow.data.main.movie.nowplayingupcoming


import com.google.gson.annotations.SerializedName

data class MovieMainResult(
    @SerializedName("id")
    val id: Int,
    @SerializedName("poster_path")
    val posterPath: String,
    @SerializedName("release_date")
    val releaseDate: String,
    @SerializedName("title")
    val title: String,
    @SerializedName("vote_average")
    val voteAverage: Double
)