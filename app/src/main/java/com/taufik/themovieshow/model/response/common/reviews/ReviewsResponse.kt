package com.taufik.themovieshow.model.response.common.reviews


import com.google.gson.annotations.SerializedName

data class ReviewsResponse(
    @SerializedName("id")
    val id: Int,
    @SerializedName("page")
    val page: Int,
    @SerializedName("results")
    val results: List<ReviewsResult>,
    @SerializedName("total_pages")
    val totalPages: Int,
    @SerializedName("total_results")
    val totalResults: Int
)