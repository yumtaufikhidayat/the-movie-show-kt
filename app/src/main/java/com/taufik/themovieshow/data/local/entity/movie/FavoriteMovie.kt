package com.taufik.themovieshow.data.local.entity.movie

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "favorite_movie")
data class FavoriteMovie(
    @PrimaryKey(autoGenerate = true)
    val movieId: Int = 0,
    @ColumnInfo(name = "poster")
    val moviePoster: String = "",
    @ColumnInfo(name = "title")
    val movieTitle: String = "",
    @ColumnInfo(name = "release_date")
    val movieReleaseData: String = "",
    @ColumnInfo(name = "rating")
    val movieRating: Double = 0.0
) : Serializable