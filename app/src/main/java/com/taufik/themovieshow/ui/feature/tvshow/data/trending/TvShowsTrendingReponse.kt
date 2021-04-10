package com.taufik.themovieshow.ui.feature.tvshow.data.trending


import com.google.gson.annotations.SerializedName

data class TvShowsTrendingReponse(
    @SerializedName("page")
    val page: Int,
    @SerializedName("results")
    val results: List<TvShowsTrendingResult>,
    @SerializedName("total_pages")
    val totalPages: Int,
    @SerializedName("total_results")
    val totalResults: Int
)