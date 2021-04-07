package com.taufik.themovieshow.ui.main.movie.data.main


import com.google.gson.annotations.SerializedName

data class MovieResponse(
    @SerializedName("dates")
        val dates: MovieDates,
    @SerializedName("page")
        val page: Int,
    @SerializedName("results")
        val results: List<MovieResult>,
    @SerializedName("total_pages")
        val totalPages: Int,
    @SerializedName("total_results")
        val totalResults: Int
)