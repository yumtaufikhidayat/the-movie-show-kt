package com.taufik.themovieshow.base.helper

interface BaseCastItem {
    val id: Int
    val name: String
    val character: String
    val profilePath: String
}

data class BaseCastItemImpl(
    override val id: Int,
    override val name: String,
    override val character: String,
    override val profilePath: String
) : BaseCastItem