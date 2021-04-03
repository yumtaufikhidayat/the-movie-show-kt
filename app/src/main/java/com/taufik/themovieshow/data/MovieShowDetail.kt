package com.taufik.themovieshow.data

data class MovieShowDetail(
        val id: Int,
        val imagePoster: String,
        val imageBackdrop: String,
        val title: String,
        val productionCompanies: String,
        val releaseDate: String,
        val status: String,
        val overview: String,
        val rate: Double,
        val genre: String,
        val runtime: String,
)
