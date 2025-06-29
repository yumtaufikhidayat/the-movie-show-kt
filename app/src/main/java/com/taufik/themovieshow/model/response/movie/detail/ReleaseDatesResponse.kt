package com.taufik.themovieshow.model.response.movie.detail

import com.google.gson.annotations.SerializedName

data class ReleaseDatesResponse(
    @SerializedName("results")
    val results: List<CountryReleaseDates>
)

data class CountryReleaseDates(
    @SerializedName("iso_3166_1")
    val iso3166_1: String,
    @SerializedName("release_dates")
    val releaseDates: List<CertificationInfo>
)

data class CertificationInfo(
    @SerializedName("certification")
    val certification: String,
    @SerializedName("release_date")
    val releaseDate: String,
    @SerializedName("type")
    val type: Int,
    @SerializedName("note")
    val note: String?
)
