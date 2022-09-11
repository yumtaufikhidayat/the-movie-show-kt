package com.taufik.themovieshow.data.main.movie.reviews


import com.google.gson.annotations.SerializedName

data class MovieReviewsResult(
    @SerializedName("author")
    val author: String,
    @SerializedName("author_details")
    val authorDetails: MovieAuthorDetails,
    @SerializedName("content")
    val content: String,
    @SerializedName("created_at")
    val createdAt: String,
    @SerializedName("id")
    val id: String,
    @SerializedName("updated_at")
    val updatedAt: String,
    @SerializedName("url")
    val url: String
)