package com.taufik.themovieshow.ui.main.tvshow.data.popular


import com.google.gson.annotations.SerializedName

data class TvShowPopularResponse(
        @SerializedName("page")
    val page: Int,
        @SerializedName("results")
    val results: List<TvShowPopularResult>,
        @SerializedName("total_pages")
    val totalPages: Int,
        @SerializedName("total_results")
    val totalResults: Int
)