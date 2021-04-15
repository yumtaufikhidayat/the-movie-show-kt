package com.taufik.themovieshow.ui.feature.movie.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "favorite_movie")
data class FavoriteMovie(
    @PrimaryKey
    val movieId: Int,
    val moviePoster: String,
    val movieTitle: String,
    val movieReleaseData: String,
    val movieRating: Double
): Serializable