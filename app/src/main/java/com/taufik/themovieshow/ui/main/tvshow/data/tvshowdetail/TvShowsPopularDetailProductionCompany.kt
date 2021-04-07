package com.taufik.themovieshow.ui.main.tvshow.data.tvshowdetail


import com.google.gson.annotations.SerializedName

data class TvShowsPopularDetailProductionCompany(
    @SerializedName("id")
    val id: Int,
    @SerializedName("logo_path")
    val logoPath: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("origin_country")
    val originCountry: String
)