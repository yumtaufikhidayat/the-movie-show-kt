package com.taufik.themovieshow.ui.main.tvshow.data.dummymain

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class DummyTvShowsMain(
    val imageBackdrop: String,
    val firstAirDate: String,
    val id: Int,
    val title: String,
    val language: String,
    val overview: String,
    val imagePoster: String,
    val rating: Double
) : Parcelable
