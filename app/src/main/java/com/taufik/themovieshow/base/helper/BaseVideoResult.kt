package com.taufik.themovieshow.base.helper

import com.taufik.themovieshow.model.response.movie.video.MovieVideoResult
import com.taufik.themovieshow.model.response.tvshow.video.TvShowsVideoResult

interface BaseVideoItem {
    val id: String
    val name: String
    val key: String
}

fun MovieVideoResult.toMovieVideoResultItem(): BaseVideoItem = object : BaseVideoItem {
    override val id: String get() = this@toMovieVideoResultItem.id
    override val name: String get() = this@toMovieVideoResultItem.name
    override val key: String get() = this@toMovieVideoResultItem.key
}

fun TvShowsVideoResult.toTvShowsVideoResultItem(): BaseVideoItem = object : BaseVideoItem {
    override val id: String get() = this@toTvShowsVideoResultItem.id
    override val name: String get() = this@toTvShowsVideoResultItem.name
    override val key: String get() = this@toTvShowsVideoResultItem.key
}