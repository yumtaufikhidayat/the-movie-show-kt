package com.taufik.themovieshow.data.main.tvshow.popularairingtoday


import com.google.gson.annotations.SerializedName

data class TvShowsMainResponse(
        @SerializedName("page")
    val page: Int,
        @SerializedName("results")
    val results: List<TvShowsMainResult>,
        @SerializedName("total_pages")
    val totalPages: Int,
        @SerializedName("total_results")
    val totalResults: Int
)