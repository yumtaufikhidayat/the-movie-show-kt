package com.taufik.themovieshow.model.response.tvshow.discover


import com.google.gson.annotations.SerializedName

data class DiscoverTvShowsResponse(
    @SerializedName("page")
    val page: Int,
    @SerializedName("results")
    val results: List<com.taufik.themovieshow.model.response.tvshow.discover.DiscoverTvShowsResult>,
    @SerializedName("total_pages")
    val totalPages: Int,
    @SerializedName("total_results")
    val totalResults: Int
)