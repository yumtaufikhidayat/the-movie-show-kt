package com.taufik.themovieshow.ui.main.movie.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.taufik.themovieshow.data.main.movie.reviews.MovieReviewsResult
import com.taufik.themovieshow.databinding.ItemReviewsBinding
import com.taufik.themovieshow.utils.loadImage

class ReviewsAdapter: ListAdapter<MovieReviewsResult, ReviewsAdapter.ReviewsViewHolder>(ReviewsDiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReviewsViewHolder {
        return ReviewsViewHolder(ItemReviewsBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ReviewsViewHolder, position: Int) = holder.bind(getItem(position))

    inner class ReviewsViewHolder(private val binding: ItemReviewsBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(data: MovieReviewsResult) = with(binding) {
            val author = data.authorDetails
            imgReviewer.loadImage(author.avatarPath)
            tvReviewerName.text = data.author
            tvReviewerRating.text = author.rating.toString()
            tvReviewerReview.text = data.content
        }
    }

    object ReviewsDiffCallback: DiffUtil.ItemCallback<MovieReviewsResult>() {
        override fun areItemsTheSame(
            oldItem: MovieReviewsResult,
            newItem: MovieReviewsResult
        ): Boolean = oldItem.id == newItem.id

        override fun areContentsTheSame(
            oldItem: MovieReviewsResult,
            newItem: MovieReviewsResult
        ): Boolean = oldItem == newItem
    }
}