package com.taufik.themovieshow.model.response.tvshow.trending


import com.google.gson.annotations.SerializedName

data class TvShowsTrendingReponse(
    @SerializedName("page")
    val page: Int,
    @SerializedName("results")
    val results: List<com.taufik.themovieshow.model.response.tvshow.trending.TvShowsTrendingResult>,
    @SerializedName("total_pages")
    val totalPages: Int,
    @SerializedName("total_results")
    val totalResults: Int
)