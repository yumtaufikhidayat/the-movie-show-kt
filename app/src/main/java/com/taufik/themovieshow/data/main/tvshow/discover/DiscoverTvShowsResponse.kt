package com.taufik.themovieshow.data.main.tvshow.discover


import com.google.gson.annotations.SerializedName

data class DiscoverTvShowsResponse(
    @SerializedName("page")
    val page: Int,
    @SerializedName("results")
    val results: List<DiscoverTvShowsResult>,
    @SerializedName("total_pages")
    val totalPages: Int,
    @SerializedName("total_results")
    val totalResults: Int
)