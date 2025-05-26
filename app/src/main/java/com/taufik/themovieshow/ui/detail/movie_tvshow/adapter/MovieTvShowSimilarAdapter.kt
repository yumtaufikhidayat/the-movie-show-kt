package com.taufik.themovieshow.ui.detail.movie_tvshow.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.taufik.themovieshow.base.helper.BaseSimilarItem
import com.taufik.themovieshow.databinding.ItemSimilarBinding
import com.taufik.themovieshow.utils.CommonDateFormatConstants
import com.taufik.themovieshow.utils.extensions.convertDate
import com.taufik.themovieshow.utils.extensions.loadImage

class MovieTvShowSimilarAdapter(
    private val onItemClickListener: (BaseSimilarItem) -> Unit
) : ListAdapter<BaseSimilarItem, MovieTvShowSimilarAdapter.SimilarViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SimilarViewHolder {
        return SimilarViewHolder(
            ItemSimilarBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: SimilarViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class SimilarViewHolder(private val binding: ItemSimilarBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: BaseSimilarItem) {
            binding.apply {
                imgPoster.loadImage(data.posterPath)
                val releaseYear = data.releaseDateText.convertDate(
                    CommonDateFormatConstants.YYYY_MM_DD_FORMAT,
                    CommonDateFormatConstants.YYYY_FORMAT
                )
                tvMovieName.text = StringBuilder(data.titleText).append("\n").append("($releaseYear)")
                cardPoster.setOnClickListener {
                    onItemClickListener(data)
                }
            }
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<BaseSimilarItem>() {
            override fun areItemsTheSame(
                oldItem: BaseSimilarItem,
                newItem: BaseSimilarItem
            ): Boolean = oldItem.id == newItem.id

            override fun areContentsTheSame(
                oldItem: BaseSimilarItem,
                newItem: BaseSimilarItem
            ): Boolean =
                oldItem.id == newItem.id &&
                        oldItem.posterPath == newItem.posterPath &&
                        oldItem.titleText == newItem.titleText &&
                        oldItem.releaseDateText == newItem.releaseDateText
        }
    }
}
