package com.taufik.themovieshow.data.local.entity.movie

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.taufik.themovieshow.utils.objects.CommonConstants
import java.io.Serializable

@Entity(tableName = CommonConstants.TABLE_NAME_FAVORITE_MOVIE_ENTITY)
data class FavoriteMovieEntity(
    @PrimaryKey(autoGenerate = true)
    val movieId: Int = 0,
    @ColumnInfo(name = CommonConstants.COLUMN_NAME_POSTER)
    val moviePoster: String = "",
    @ColumnInfo(name = CommonConstants.COLUMN_NAME_TITLE)
    val movieTitle: String = "",
    @ColumnInfo(name = CommonConstants.COLUMN_NAME_RELEASE_DATE)
    val movieReleaseData: String = "",
    @ColumnInfo(name = CommonConstants.COLUMN_NAME_RATING)
    val movieRating: Double = 0.0
) : Serializable