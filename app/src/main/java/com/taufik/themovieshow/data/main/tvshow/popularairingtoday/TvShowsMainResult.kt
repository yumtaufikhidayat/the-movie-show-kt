package com.taufik.themovieshow.data.main.tvshow.popularairingtoday


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class TvShowsMainResult(
    @SerializedName("first_air_date")
    val firstAirDate: String?,
    @SerializedName("id")
    val id: Int,
    @SerializedName("name")
    val name: String,
    @SerializedName("poster_path")
    val posterPath: String,
    @SerializedName("vote_average")
    val voteAverage: Double
): Parcelable