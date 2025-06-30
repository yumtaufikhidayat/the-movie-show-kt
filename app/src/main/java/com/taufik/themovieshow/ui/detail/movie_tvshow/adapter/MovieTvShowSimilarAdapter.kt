package com.taufik.themovieshow.ui.detail.movie_tvshow.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.taufik.themovieshow.base.helper.BaseSimilarItem
import com.taufik.themovieshow.databinding.ItemSimilarMovieAndCastBinding
import com.taufik.themovieshow.utils.extensions.applyMiddleMargins
import com.taufik.themovieshow.utils.extensions.convertDate
import com.taufik.themovieshow.utils.extensions.convertDpToPx
import com.taufik.themovieshow.utils.extensions.loadImage
import com.taufik.themovieshow.utils.objects.CommonDateFormatConstants

class MovieTvShowSimilarAdapter(
    private val onItemClickListener: (BaseSimilarItem) -> Unit
) : ListAdapter<BaseSimilarItem, MovieTvShowSimilarAdapter.SimilarViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SimilarViewHolder {
        return SimilarViewHolder(
            ItemSimilarMovieAndCastBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: SimilarViewHolder, position: Int) {
        holder.bind(getItem(position), position, currentList.size)
    }

    inner class SimilarViewHolder(private val binding: ItemSimilarMovieAndCastBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: BaseSimilarItem, position: Int, itemCount: Int) {
            binding.apply {
                imgPoster.loadImage(data.posterPath)
                val releaseYear = data.releaseDateText.convertDate(
                    CommonDateFormatConstants.YYYY_MM_DD_FORMAT,
                    CommonDateFormatConstants.YYYY_FORMAT
                )
                tvMovieOrCastName.text = StringBuilder(data.titleText).append("\n").append("($releaseYear)")

                layoutSimilarMovieOrCast.apply {
                    val marginParam = layoutParams as ViewGroup.MarginLayoutParams
                    marginParam.applyMiddleMargins(
                        position = position,
                        itemCount = itemCount,
                        middle = 4,
                        convertDpToPx = ::convertDpToPx
                    )

                    setOnClickListener {
                        onItemClickListener(data)
                    }
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
