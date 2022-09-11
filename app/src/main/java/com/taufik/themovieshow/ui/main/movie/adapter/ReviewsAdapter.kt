package com.taufik.themovieshow.ui.main.movie.adapter

import android.content.Intent
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.taufik.themovieshow.data.main.movie.reviews.MovieReviewsResult
import com.taufik.themovieshow.databinding.ItemReviewsBinding
import com.taufik.themovieshow.utils.CommonDateFormatConstants
import com.taufik.themovieshow.utils.convertDate
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
            tvReviewerRating.text = String.format("%s%s", author.rating.toString(), "/10")
            tvReviewerDate.text = data.updatedAt.convertDate(
                CommonDateFormatConstants.FULL_FORMAT,
                CommonDateFormatConstants.EEE_D_MMM_YYYY_FORMAT
            )
            tvReviewerReview.text = data.content
            cardReview.setOnClickListener {
                try {
                    val intentBrowser = Intent(Intent.ACTION_VIEW, Uri.parse(data.url))
                    it.context.startActivity(Intent.createChooser(intentBrowser, "Open with:"))
                } catch (e: java.lang.Exception) {
                    Log.e("errorIntent", "onBindViewHolder: ${e.localizedMessage}")
                }
            }
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