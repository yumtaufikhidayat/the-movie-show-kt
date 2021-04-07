package com.taufik.themovieshow.ui.main.tvshow.data.populardetail


import com.google.gson.annotations.SerializedName

data class TvShowsPopularDetailProductionCountry(
    @SerializedName("iso_3166_1")
    val iso31661: String,
    @SerializedName("name")
    val name: String
)