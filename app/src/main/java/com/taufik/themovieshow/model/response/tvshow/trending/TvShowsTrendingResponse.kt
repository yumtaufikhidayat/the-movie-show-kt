package com.taufik.themovieshow.model.response.tvshow.trending


import com.google.gson.annotations.SerializedName

data class TvShowsTrendingResponse(
    @SerializedName("page")
    val page: Int,
    @SerializedName("results")
    val results: List<TvShowsTrendingResult>,
    @SerializedName("total_pages")
    val totalPages: Int,
    @SerializedName("total_results")
    val totalResults: Int
)