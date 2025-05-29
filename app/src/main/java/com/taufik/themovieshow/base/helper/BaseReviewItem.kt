package com.taufik.themovieshow.base.helper

import com.taufik.themovieshow.model.response.common.reviews.ReviewsResult

interface BaseReviewItem {
    val id: String
    val author: String
    val authorAvatar: String
    val authorRating: String
    val updatedAt: String
    val content: String
    val url: String
}

fun ReviewsResult.toBaseReviewItem(): BaseReviewItem = object : BaseReviewItem {
    override val id get() = this@toBaseReviewItem.id
    override val author get() = this@toBaseReviewItem.author
    override val authorAvatar get() = this@toBaseReviewItem.authorDetails.avatarPath
    override val authorRating get() = this@toBaseReviewItem.authorDetails.rating.toString()
    override val updatedAt get() = this@toBaseReviewItem.updatedAt
    override val content get() = this@toBaseReviewItem.content
    override val url get() = this@toBaseReviewItem.url
}