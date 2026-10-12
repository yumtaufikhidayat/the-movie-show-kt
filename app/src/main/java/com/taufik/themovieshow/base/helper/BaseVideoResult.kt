package com.taufik.themovieshow.base.helper

import com.taufik.themovieshow.model.response.movie.video.MovieVideoResult
import com.taufik.themovieshow.model.response.tvshow.video.TvShowsVideoResult

interface BaseVideoItem {
    val id: String
    val name: String
    val key: String
}

fun MovieVideoResult.toMovieVideoResultItem(): BaseVideoItem = object : BaseVideoItem {
    override val id: String get() = this@toMovieVideoResultItem.id.orEmpty()
    override val name: String get() = this@toMovieVideoResultItem.name.orEmpty()
    override val key: String get() = this@toMovieVideoResultItem.key.orEmpty()
}

fun TvShowsVideoResult.toTvShowsVideoResultItem(): BaseVideoItem = object : BaseVideoItem {
    override val id: String get() = this@toTvShowsVideoResultItem.id.orEmpty()
    override val name: String get() = this@toTvShowsVideoResultItem.name.orEmpty()
    override val key: String get() = this@toTvShowsVideoResultItem.key.orEmpty()
}