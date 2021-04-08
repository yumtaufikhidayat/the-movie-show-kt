package com.taufik.themovieshow.ui.main.tvshow.data.detail


import com.google.gson.annotations.SerializedName

data class TvShowsPopularDetailSpokenLanguage(
    @SerializedName("english_name")
    val englishName: String,
    @SerializedName("iso_639_1")
    val iso6391: String,
    @SerializedName("name")
    val name: String
)