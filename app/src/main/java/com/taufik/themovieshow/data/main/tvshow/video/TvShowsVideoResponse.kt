package com.taufik.themovieshow.data.main.tvshow.video


import com.google.gson.annotations.SerializedName

data class TvShowsVideoResponse(
    @SerializedName("id")
    val id: Int,
    @SerializedName("results")
    val results: List<TvShowsVideoResult>
)