package com.taufik.themovieshow.ui.main.movie.data.dummymain

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class DummyMovieMain(
    val posterPath: String,
    val id: Int,
    val language: String,
    val overview: String,
    val imagePoster: String,
    val releaseDate: String,
    val title: String,
    val rating: Double
) : Parcelable
