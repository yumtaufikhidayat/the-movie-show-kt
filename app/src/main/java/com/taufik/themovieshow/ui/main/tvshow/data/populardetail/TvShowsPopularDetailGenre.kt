package com.taufik.themovieshow.ui.main.tvshow.data.populardetail


import com.google.gson.annotations.SerializedName

data class TvShowsPopularDetailGenre(
    @SerializedName("id")
    val id: Int,
    @SerializedName("name")
    val name: String
)