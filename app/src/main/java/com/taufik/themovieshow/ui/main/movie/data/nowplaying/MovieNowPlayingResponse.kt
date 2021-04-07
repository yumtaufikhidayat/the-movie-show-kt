package com.taufik.themovieshow.ui.main.movie.data.nowplaying


import com.google.gson.annotations.SerializedName

data class MovieNowPlayingResponse(
        @SerializedName("dates")
        val dates: MovieNowPlayingDates,
        @SerializedName("page")
        val page: Int,
        @SerializedName("results")
        val results: List<MovieNowPlayingResult>,
        @SerializedName("total_pages")
        val totalPages: Int,
        @SerializedName("total_results")
        val totalResults: Int
)