package com.taufik.themovieshow.base.helper

import com.taufik.themovieshow.model.response.movie.similar.MovieSimilarResult
import com.taufik.themovieshow.model.response.tvshow.similar.TvShowsSimilarResultsItem

interface BaseSimilarItem {
    val id: Int
    val posterPath: String?
    val titleText: String
    val releaseDateText: String
}

fun MovieSimilarResult.toMovieBaseSimilarItem(): BaseSimilarItem = object : BaseSimilarItem {
    override val id: Int get() = this@toMovieBaseSimilarItem.id
    override val posterPath: String get() = this@toMovieBaseSimilarItem.posterPath.orEmpty()
    override val titleText: String get() = this@toMovieBaseSimilarItem.title
    override val releaseDateText: String get() = this@toMovieBaseSimilarItem.releaseDate
}

fun TvShowsSimilarResultsItem.toTvShowSimilarItem(): BaseSimilarItem = object : BaseSimilarItem {
    override val id: Int get() = this@toTvShowSimilarItem.id
    override val posterPath: String? get() = this@toTvShowSimilarItem.posterPath
    override val titleText: String get() = this@toTvShowSimilarItem.originalName
    override val releaseDateText: String get() = this@toTvShowSimilarItem.firstAirDate
}