package com.taufik.themovieshow.model.response.movie.nowplayingupcoming


import com.google.gson.annotations.SerializedName

data class MovieMainResponse(
        @SerializedName("dates")
        val dates: com.taufik.themovieshow.model.response.movie.nowplayingupcoming.MovieMainDates,
        @SerializedName("page")
        val page: Int,
        @SerializedName("results")
        val results: List<com.taufik.themovieshow.model.response.movie.nowplayingupcoming.MovieMainResult>,
        @SerializedName("total_pages")
        val totalPages: Int,
        @SerializedName("total_results")
        val totalResults: Int
)