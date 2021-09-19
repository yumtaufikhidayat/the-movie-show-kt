package com.taufik.themovieshow.ui.tvshow.model.video


import com.google.gson.annotations.SerializedName

data class TvShowsVideoResponse(
    @SerializedName("id")
    val id: Int,
    @SerializedName("results")
    val results: List<TvShowsVideoResult>
)