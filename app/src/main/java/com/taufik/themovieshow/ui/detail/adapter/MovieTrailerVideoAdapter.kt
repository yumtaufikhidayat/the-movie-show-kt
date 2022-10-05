package com.taufik.themovieshow.ui.detail.adapter


import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.taufik.themovieshow.data.main.movie.video.MovieVideoResult
import com.taufik.themovieshow.databinding.ItemTrailerVideoBinding
import com.taufik.themovieshow.utils.loadVideoThumbnail

class MovieTrailerVideoAdapter: ListAdapter<MovieVideoResult, MovieTrailerVideoAdapter.MovieTrailerViewHolder>(MovieTrailerDiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieTrailerViewHolder {
        return MovieTrailerViewHolder(ItemTrailerVideoBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: MovieTrailerViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class MovieTrailerViewHolder(val binding: ItemTrailerVideoBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(data: MovieVideoResult) = with(binding) {
            imgPoster.loadVideoThumbnail(data.key)
            tvTitle.text = data.name
            cardTrailerVideo.setOnClickListener {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube://${data.key}"))
                itemView.context.startActivity(intent)
            }
        }
    }

    object MovieTrailerDiffCallback: DiffUtil.ItemCallback<MovieVideoResult>(){
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