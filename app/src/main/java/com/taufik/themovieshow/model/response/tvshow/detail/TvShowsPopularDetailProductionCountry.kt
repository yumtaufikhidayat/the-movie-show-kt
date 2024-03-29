package com.taufik.themovieshow.model.response.tvshow.detail


import com.google.gson.annotations.SerializedName

data class TvShowsPopularDetailProductionCountry(
    @SerializedName("iso_3166_1")
    val iso31661: String,
    @SerializedName("name")
    val name: String
)