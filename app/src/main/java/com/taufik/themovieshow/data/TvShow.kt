package com.taufik.themovieshow.data

data class TvShow(
    val id: Int,
    val imagePoster: String,
    val title: String,
    val network: String,
    val releaseDate: String,
    val status: String
)
