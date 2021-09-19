package com.taufik.themovieshow.ui.tvshow.model.cast


import com.google.gson.annotations.SerializedName

data class TvShowsCastResponse(
    @SerializedName("cast")
    val cast: List<TvShowsCast>,
    @SerializedName("crew")
    val crew: List<TvShowsCastCrew>,
    @SerializedName("id")
    val id: Int
)