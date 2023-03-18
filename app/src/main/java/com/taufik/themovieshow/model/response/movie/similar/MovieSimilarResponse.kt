package com.taufik.themovieshow.model.response.movie.similar

import com.google.gson.annotations.SerializedName

data class MovieSimilarResponse(
    @SerializedName("page")
    val page: Int,
    @SerializedName("results")
    val results: List<MovieSimilarResult>,
    @SerializedName("total_pages")
    val totalPages: Int,
    @SerializedName("total_results")
    val totalResults: Int
)