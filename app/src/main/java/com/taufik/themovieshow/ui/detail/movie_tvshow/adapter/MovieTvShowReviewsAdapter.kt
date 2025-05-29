package com.taufik.themovieshow.ui.detail.movie_tvshow.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.taufik.themovieshow.base.helper.BaseReviewItem
import com.taufik.themovieshow.databinding.ItemReviewsBinding
import com.taufik.themovieshow.utils.extensions.convertDate
import com.taufik.themovieshow.utils.extensions.loadImage
import com.taufik.themovieshow.utils.objects.CommonDateFormatConstants

class MovieTvShowReviewsAdapter(
    private val onClickListener: (String) -> Unit
) : ListAdapter<BaseReviewItem, MovieTvShowReviewsAdapter.ReviewsViewHolder>(REVIEWS_DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReviewsViewHolder {
        return ReviewsViewHolder(
            ItemReviewsBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ReviewsViewHolder, position: Int) =
        holder.bind(getItem(position))

    inner class ReviewsViewHolder(private val binding: ItemReviewsBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: BaseReviewItem) {
            binding.apply {
                imgReviewer.loadImage(data.authorAvatar)
                tvReviewerName.text = data.author
                tvReviewerRating.text = String.format("%s%s", data.authorRating.toString(), "/10")
                tvReviewerDate.text = data.updatedAt.convertDate(
                    CommonDateFormatConstants.FULL_FORMAT,
                    CommonDateFormatConstants.EEE_D_MMM_YYYY_FORMAT
                )
                tvReviewerReview.text = data.content
                cardReview.setOnClickListener {
                    onClickListener.invoke(data.url)
                }
            }
        }
    }

    companion object {
        val REVIEWS_DIFF_CALLBACK = object :
            DiffUtil.ItemCallback<BaseReviewItem>() {
            override fun areItemsTheSame(
                oldItem: BaseReviewItem,
                newItem: BaseReviewItem
            ): Boolean = oldItem.id == newItem.id

            override fun areContentsTheSame(
                oldItem: BaseReviewItem,
                newItem: BaseReviewItem
            ): Boolean =
                oldItem.id == newItem.id &&
                        oldItem.author == newItem.author &&
                        oldItem.authorAvatar == newItem.authorAvatar &&
                        oldItem.authorRating == newItem.authorRating &&
                        oldItem.updatedAt == newItem.updatedAt &&
                        oldItem.content == newItem.content &&
                        oldItem.url == newItem.url
        }
    }
}