package com.taufik.themovieshow.ui.detail.movie_tvshow.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.taufik.themovieshow.R
import com.taufik.themovieshow.base.helper.BaseCastItem
import com.taufik.themovieshow.databinding.ItemCastBinding
import com.taufik.themovieshow.utils.extensions.loadImage

class MovieTvShowCastAdapter : ListAdapter<BaseCastItem, MovieTvShowCastAdapter.CastViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CastViewHolder {
        return CastViewHolder(
            ItemCastBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: CastViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class CastViewHolder(private val binding: ItemCastBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: BaseCastItem) {
            binding.apply {
                imgPoster.loadImage(data.profilePath)
                tvCastName.text = if (data.character.isNotEmpty()) {
                    root.context.getString(R.string.tvNameCharacter, data.name, data.character)
                } else {
                    root.context.getString(
                        R.string.tvNameCharacter,
                        data.name,
                        root.context.getString(R.string.tvNA)
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
