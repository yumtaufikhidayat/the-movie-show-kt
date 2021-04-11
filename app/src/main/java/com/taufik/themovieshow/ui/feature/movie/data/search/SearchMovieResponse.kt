package com.taufik.themovieshow.ui.feature.movie.data.search


import com.google.gson.annotations.SerializedName

data class SearchMovieResponse(
        @SerializedName("page")
    val page: Int,
        @SerializedName("results")
    val results: List<SearchMovieResult>,
        @SerializedName("total_pages")
    val totalPages: Int,
        @SerializedName("total_results")
    val totalResults: Int
)