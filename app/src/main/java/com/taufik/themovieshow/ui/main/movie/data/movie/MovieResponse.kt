package com.taufik.themovieshow.ui.main.movie.data.movie


import com.google.gson.annotations.SerializedName

data class MovieResponse(
        @SerializedName("dates")
        val dates: Dates,
        @SerializedName("page")
        val page: Int,
        @SerializedName("results")
        val results: List<MovieResult>,
        @SerializedName("total_pages")
        val totalPages: Int,
        @SerializedName("total_results")
        val totalResults: Int
)