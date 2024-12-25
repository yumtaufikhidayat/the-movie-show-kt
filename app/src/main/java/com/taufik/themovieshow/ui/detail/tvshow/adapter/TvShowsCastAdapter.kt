package com.taufik.themovieshow.ui.detail.tvshow.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.taufik.themovieshow.R
import com.taufik.themovieshow.databinding.ItemCastBinding
import com.taufik.themovieshow.model.response.tvshow.cast.TvShowsCast
import com.taufik.themovieshow.utils.extensions.loadImage

class TvShowsCastAdapter : ListAdapter<TvShowsCast, TvShowsCastAdapter.MovieViewHolder>(TV_SHOW_CAST_DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        return MovieViewHolder(
            ItemCastBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class MovieViewHolder(private val binding: ItemCastBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: TvShowsCast) {
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
        val TV_SHOW_CAST_DIFF_CALLBACK = object :
            DiffUtil.ItemCallback<TvShowsCast>() {
            override fun areItemsTheSame(
                oldItem: TvShowsCast,
                newItem: TvShowsCast
            ): Boolean = oldItem.id == newItem.id

            override fun areContentsTheSame(
                oldItem: TvShowsCast,
                newItem: TvShowsCast
            ): Boolean = oldItem == newItem
        }
    }
}