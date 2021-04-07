package com.taufik.themovieshow.ui.main.tvshow.data.populardetail


import com.google.gson.annotations.SerializedName

data class TvShowsPopularDetailCreatedBy(
    @SerializedName("credit_id")
    val creditId: String,
    @SerializedName("gender")
    val gender: Int,
    @SerializedName("id")
    val id: Int,
    @SerializedName("name")
    val name: String,
    @SerializedName("profile_path")
    val profilePath: Any
)