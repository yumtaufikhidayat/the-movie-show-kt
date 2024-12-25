package com.taufik.themovieshow.ui.detail.movie.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.taufik.themovieshow.databinding.ItemTrailerVideoBinding
import com.taufik.themovieshow.model.response.movie.video.MovieVideoResult
import com.taufik.themovieshow.utils.extensions.loadVideoThumbnail

class MovieTrailerVideoAdapter(
    private val onItemClickListener: (MovieVideoResult) -> Unit
) : ListAdapter<MovieVideoResult, MovieTrailerVideoAdapter.MovieTrailerViewHolder>(
    MOVIE_TRAILER_DIFF_CALLBACK
) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieTrailerViewHolder {
        return MovieTrailerViewHolder(
            ItemTrailerVideoBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: MovieTrailerViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class MovieTrailerViewHolder(val binding: ItemTrailerVideoBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: MovieVideoResult) {
            binding.apply {
                imgPoster.loadVideoThumbnail(data.key)
                tvTitle.text = data.name
                cardTrailerVideo.setOnClickListener {
                    onItemClickListener(data)
                }
            }
        }
    }

    companion object {
        val MOVIE_TRAILER_DIFF_CALLBACK = object :
            DiffUtil.ItemCallback<MovieVideoResult>() {
            override fun areItemsTheSame(
                oldItem: MovieVideoResult,
                newItem: MovieVideoResult
            ): Boolean = oldItem == newItem

            override fun areContentsTheSame(
                oldItem: MovieVideoResult,
                newItem: MovieVideoResult
            ): Boolean = oldItem.id == newItem.id
        }
    }
}