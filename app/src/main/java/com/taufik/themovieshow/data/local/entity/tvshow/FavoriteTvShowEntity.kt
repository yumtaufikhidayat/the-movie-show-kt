package com.taufik.themovieshow.data.local.entity.tvshow

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.taufik.themovieshow.utils.objects.CommonConstants
import java.io.Serializable

@Entity(tableName = CommonConstants.TABLE_NAME_FAVORITE_TV_SHOW_ENTITY)
data class FavoriteTvShowEntity(
    @PrimaryKey(autoGenerate = true)
    val tvShowId: Int = 0,
    @ColumnInfo(name = CommonConstants.COLUMN_NAME_POSTER)
    val tvShowPoster: String = "",
    @ColumnInfo(name = CommonConstants.COLUMN_NAME_TITLE)
    val tvShowTitle: String = "",
    @ColumnInfo(name = CommonConstants.COLUMN_NAME_FIRST_AIR_DATE)
    val tvShowFirstAirDate: String = "",
    @ColumnInfo(name = CommonConstants.COLUMN_NAME_RATING)
    val tvShowRating: Double = 0.0
) : Serializable