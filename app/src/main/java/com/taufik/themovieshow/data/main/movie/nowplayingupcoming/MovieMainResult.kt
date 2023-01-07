package com.taufik.themovieshow.data.main.movie.nowplayingupcoming


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class MovieMainResult(
    @SerializedName("id")
    val id: Int,
    @SerializedName("poster_path")
    val posterPath: String,
    @SerializedName("release_date")
    val releaseDate: String?,
    @SerializedName("title")
    val title: String,
    @SerializedName("vote_average")
    val voteAverage: Double
): Parcelable