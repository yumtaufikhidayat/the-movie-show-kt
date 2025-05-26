package com.taufik.themovieshow.ui.detail.movie_tvshow.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.taufik.themovieshow.base.helper.BaseVideoItem
import com.taufik.themovieshow.databinding.ItemTrailerVideoBinding
import com.taufik.themovieshow.utils.extensions.loadVideoThumbnail

class MovieTvShowTrailerVideoAdapter(
    private val onItemClickListener: (BaseVideoItem) -> Unit
) : ListAdapter<BaseVideoItem, MovieTvShowTrailerVideoAdapter.TrailerViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrailerViewHolder {
        return TrailerViewHolder(
            ItemTrailerVideoBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: TrailerViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class TrailerViewHolder(private val binding: ItemTrailerVideoBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: BaseVideoItem) {
            binding.apply {
                imgPoster.loadVideoThumbnail(data.key)
                tvTitle.text = data.name
                cardTrailerVideo.setOnClickListener {
                    onItemClickListener.invoke(data)
                }
            }
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<BaseVideoItem>() {
            override fun areItemsTheSame(
                oldItem: BaseVideoItem,
                newItem: BaseVideoItem
            ): Boolean = oldItem.id == newItem.id

            override fun areContentsTheSame(
                oldItem: BaseVideoItem,
                newItem: BaseVideoItem
            ): Boolean = oldItem.id == newItem.id &&
                    oldItem.name == newItem.name &&
                    oldItem.key == newItem.key
        }
    }
}