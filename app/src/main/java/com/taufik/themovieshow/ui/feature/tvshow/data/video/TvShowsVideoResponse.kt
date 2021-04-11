package com.taufik.themovieshow.ui.feature.tvshow.data.video


import com.google.gson.annotations.SerializedName

data class TvShowsVideoResponse(
    @SerializedName("id")
    val id: Int,
    @SerializedName("results")
    val results: List<TvShowsVideoResult>
)