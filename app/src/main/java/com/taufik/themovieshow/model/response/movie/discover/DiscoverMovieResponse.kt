package com.taufik.themovieshow.model.response.movie.discover


import com.google.gson.annotations.SerializedName

data class DiscoverMovieResponse(
    @SerializedName("page")
    val page: Int,
    @SerializedName("results")
    val results: List<com.taufik.themovieshow.model.response.movie.discover.DiscoverMovieResult>,
    @SerializedName("total_pages")
    val totalPages: Int,
    @SerializedName("total_results")
    val totalResults: Int
)