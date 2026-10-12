package com.taufik.themovieshow.base.helper

import com.taufik.themovieshow.model.response.common.reviews.ReviewsResult

interface BaseReviewItem {
    val id: String
    val author: String
    val authorAvatar: String?
    val authorRating: String
    val updatedAt: String
    val content: String
    val url: String
}

fun ReviewsResult.toBaseReviewItem(): BaseReviewItem = object : BaseReviewItem {
    override val id get() = this@toBaseReviewItem.id.orEmpty()
    override val author get() = this@toBaseReviewItem.author.orEmpty()
    override val authorAvatar get() = this@toBaseReviewItem.authorDetails?.avatarPath
    override val authorRating get() = this@toBaseReviewItem.authorDetails?.rating?.toString().orEmpty()
    override val updatedAt get() = this@toBaseReviewItem.updatedAt.orEmpty()
    override val content get() = this@toBaseReviewItem.content.orEmpty()
    override val url get() = this@toBaseReviewItem.url.orEmpty()
}
