package com.taufik.themovieshow.base.helper

import com.taufik.themovieshow.model.response.movie.cast.MovieCast
import com.taufik.themovieshow.model.response.tvshow.cast.TvShowsCast
import kotlin.Int

interface BaseCastItem {
    val id: Int
    val name: String
    val character: String
    val profilePath: String?
}

fun MovieCast.toCastItem(): BaseCastItem = object : BaseCastItem {
    override val id = this@toCastItem.id
    override val name = this@toCastItem.name
    override val character = this@toCastItem.character
    override val profilePath = this@toCastItem.profilePath
}

fun TvShowsCast.toCastItem(): BaseCastItem = object : BaseCastItem {
    override val id = this@toCastItem.id
    override val name = this@toCastItem.name
    override val character = this@toCastItem.character
    override val profilePath = this@toCastItem.profilePath
}