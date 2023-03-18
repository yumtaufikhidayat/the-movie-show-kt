package com.taufik.themovieshow.model.response.tvshow.similar

import com.google.gson.annotations.SerializedName

data class TvShowsSimilarResponse(

    @field:SerializedName("page")
    val page: Int,

    @field:SerializedName("total_pages")
    val totalPages: Int,

    @field:SerializedName("results")
    val results: List<com.taufik.themovieshow.model.response.tvshow.similar.TvShowsSimilarResultsItem>,

    @field:SerializedName("total_results")
    val totalResults: Int
)