package com.taufik.themovieshow.data.main.movie.trending


import com.google.gson.annotations.SerializedName

data class MovieTrendingResponse(
        @SerializedName("page")
        val page: Int,
        @SerializedName("results")
        val results: List<MovieTrendingResult>,
        @SerializedName("total_pages")
        val totalPages: Int,
        @SerializedName("total_results")
        val totalResults: Int
)