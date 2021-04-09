package com.taufik.themovieshow.ui.feature.tvshow.data.detail


import com.google.gson.annotations.SerializedName

data class TvShowsPopularDetailGenre(
    @SerializedName("id")
    val id: Int,
    @SerializedName("name")
    val name: String
)