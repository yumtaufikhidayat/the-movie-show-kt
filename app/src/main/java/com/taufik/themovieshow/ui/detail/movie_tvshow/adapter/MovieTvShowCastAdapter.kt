package com.taufik.themovieshow.ui.detail.movie_tvshow.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.taufik.themovieshow.R
import com.taufik.themovieshow.base.helper.BaseCastItem
import com.taufik.themovieshow.databinding.ItemSimilarMovieAndCastBinding
import com.taufik.themovieshow.utils.extensions.applyMiddleMargins
import com.taufik.themovieshow.utils.extensions.convertDpToPx
import com.taufik.themovieshow.utils.extensions.loadImage

class MovieTvShowCastAdapter : ListAdapter<BaseCastItem, MovieTvShowCastAdapter.CastViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CastViewHolder {
        return CastViewHolder(
            ItemSimilarMovieAndCastBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: CastViewHolder, position: Int) {
        holder.bind(getItem(position), position, currentList.size)
    }

    inner class CastViewHolder(private val binding: ItemSimilarMovieAndCastBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: BaseCastItem, position: Int, itemCount: Int) {
            binding.apply {
                imgPoster.loadImage(data.profilePath)
                tvMovieOrCastName.text = if (data.character.isNotEmpty()) {
                    root.context.getString(R.string.tvNameCharacter, data.name, data.character)
                } else {
                    root.context.getString(
                        R.string.tvNameCharacter,
                        data.name,
                        root.context.getString(R.string.tvNA)
                    )
                }

                layoutSimilarMovieOrCast.apply {
                    val marginParam = layoutParams as ViewGroup.MarginLayoutParams
                    marginParam.applyMiddleMargins(
                        position = position,
                        itemCount = itemCount,
                        middle = 4,
                        convertDpToPx = ::convertDpToPx
                    )
                }
            }
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<BaseCastItem>() {
            override fun areItemsTheSame(
                oldItem: BaseCastItem,
                newItem: BaseCastItem
            ): Boolean = oldItem.id == newItem.id

            override fun areContentsTheSame(
                oldItem: BaseCastItem,
                newItem: BaseCastItem
            ): Boolean =
                oldItem.id == newItem.id &&
                        oldItem.name == newItem.name &&
                        oldItem.character == newItem.character &&
                        oldItem.profilePath == newItem.profilePath
        }
    }
}
