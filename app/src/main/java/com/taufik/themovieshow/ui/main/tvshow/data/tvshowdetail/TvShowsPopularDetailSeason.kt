package com.taufik.themovieshow.ui.main.tvshow.data.tvshowdetail


import com.google.gson.annotations.SerializedName

data class TvShowsPopularDetailSeason(
    @SerializedName("air_date")
    val airDate: String,
    @SerializedName("episode_count")
    val episodeCount: Int,
    @SerializedName("id")
    val id: Int,
    @SerializedName("name")
    val name: String,
    @SerializedName("overview")
    val overview: String,
    @SerializedName("poster_path")
    val posterPath: String,
    @SerializedName("season_number")
    val seasonNumber: Int
)