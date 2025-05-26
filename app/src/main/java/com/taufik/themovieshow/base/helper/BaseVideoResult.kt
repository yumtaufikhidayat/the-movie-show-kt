package com.taufik.themovieshow.base.helper

interface BaseVideoItem {
    val id: String
    val name: String
    val key: String
}

data class BaseVideoItemImpl(
    override val id: String,
    override val name: String,
    override val key: String
) : BaseVideoItem