package com.taufik.themovieshow.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "favorite_tv_show")
data class FavoriteTvShow(
    @PrimaryKey
    val tvShowId: Int,
    val tvShowPoster: String,
    val tvShowTitle: String,
    val tvShowFirstAirDate: String,
    val tvShowRating: Double
): Serializable