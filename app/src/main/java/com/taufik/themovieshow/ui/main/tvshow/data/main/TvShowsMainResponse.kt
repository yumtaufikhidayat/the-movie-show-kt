package com.taufik.themovieshow.ui.main.tvshow.data.main


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