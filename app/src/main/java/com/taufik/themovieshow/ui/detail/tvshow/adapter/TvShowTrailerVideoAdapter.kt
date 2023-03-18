package com.taufik.themovieshow.ui.detail.tvshow.adapter

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.taufik.themovieshow.databinding.ItemTrailerVideoBinding
import com.taufik.themovieshow.model.response.tvshow.video.TvShowsVideoResult
import com.taufik.themovieshow.utils.loadVideoThumbnail

class TvShowTrailerVideoAdapter :
    ListAdapter<TvShowsVideoResult, TvShowTrailerVideoAdapter.TvShowTrailerViewHolder>(TvShowTrailerDiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TvShowTrailerViewHolder {
        return TvShowTrailerViewHolder(
            ItemTrailerVideoBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: TvShowTrailerViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class TvShowTrailerViewHolder(private val binding: ItemTrailerVideoBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: TvShowsVideoResult) {
            binding.apply {
                imgPoster.loadVideoThumbnail(data.key)
                tvTitle.text = data.name
                cardTrailerVideo.setOnClickListener {
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube://${data.key}"))
                    itemView.context.startActivity(intent)
                }
            }
        }
    }

    object TvShowTrailerDiffCallback :
        DiffUtil.ItemCallback<TvShowsVideoResult>() {
        override fun areItemsTheSame(
            oldItem: TvShowsVideoResult,
            newItem: TvShowsVideoResult
        ): Boolean = oldItem == newItem

        override fun areContentsTheSame(
            oldItem: TvShowsVideoResult,
            newItem: TvShowsVideoResult
        ): Boolean = oldItem.id == newItem.id
    }
}