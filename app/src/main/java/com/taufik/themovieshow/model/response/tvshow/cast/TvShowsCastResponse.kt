package com.taufik.themovieshow.model.response.tvshow.cast


import com.google.gson.annotations.SerializedName

data class TvShowsCastResponse(
    @SerializedName("cast")
    val cast: List<com.taufik.themovieshow.model.response.tvshow.cast.TvShowsCast>,
    @SerializedName("crew")
    val crew: List<com.taufik.themovieshow.model.response.tvshow.cast.TvShowsCastCrew>,
    @SerializedName("id")
    val id: Int
)