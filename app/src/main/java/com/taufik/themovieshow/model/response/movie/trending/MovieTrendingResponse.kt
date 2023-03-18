package com.taufik.themovieshow.model.response.movie.trending


import com.google.gson.annotations.SerializedName

data class MovieTrendingResponse(
        @SerializedName("page")
        val page: Int,
        @SerializedName("results")
        val results: List<com.taufik.themovieshow.model.response.movie.trending.MovieTrendingResult>,
        @SerializedName("total_pages")
        val totalPages: Int,
        @SerializedName("total_results")
        val totalResults: Int
)