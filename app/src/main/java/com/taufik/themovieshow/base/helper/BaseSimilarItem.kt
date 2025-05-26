package com.taufik.themovieshow.base.helper

interface BaseSimilarItem {
    val id: Int
    val posterPath: String
    val titleText: String
    val releaseDateText: String
}

data class BaseSimilarItemImpl(
    override val id: Int,
    override val posterPath: String,
    override val titleText: String,
    override val releaseDateText: String
) : BaseSimilarItem