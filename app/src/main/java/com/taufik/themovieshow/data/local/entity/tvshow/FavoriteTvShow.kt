package com.taufik.themovieshow.data.local.entity.tvshow

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "favorite_tv_show")
data class FavoriteTvShow(
    @PrimaryKey(autoGenerate = true)
    val tvShowId: Int = 0,
    @ColumnInfo(name = "poster")
    val tvShowPoster: String = "",
    @ColumnInfo(name = "title")
    val tvShowTitle: String = "",
    @ColumnInfo(name = "first_air_date")
    val tvShowFirstAirDate: String = "",
    @ColumnInfo(name = "rating")
    val tvShowRating: Double = 0.0
) : Serializable