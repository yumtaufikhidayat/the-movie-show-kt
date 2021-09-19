package com.taufik.themovieshow.ui.tvshow.model.popularairingtoday


import com.google.gson.annotations.SerializedName

data class TvShowsMainResult(
    @SerializedName("first_air_date")
    val firstAirDate: String,
    @SerializedName("id")
    val id: Int,
    @SerializedName("name")
    val name: String,
    @SerializedName("poster_path")
    val posterPath: String,
    @SerializedName("vote_average")
    val voteAverage: Double
)