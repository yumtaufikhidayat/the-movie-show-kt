package com.taufik.themovieshow.ui.movie.model.nowplayingupcoming


import com.google.gson.annotations.SerializedName

data class MovieMainResponse(
        @SerializedName("dates")
        val dates: MovieMainDates,
        @SerializedName("page")
        val page: Int,
        @SerializedName("results")
        val results: List<MovieMainResult>,
        @SerializedName("total_pages")
        val totalPages: Int,
        @SerializedName("total_results")
        val totalResults: Int
)