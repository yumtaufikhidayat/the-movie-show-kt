package com.taufik.themovieshow.ui.main.movie.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.taufik.themovieshow.data.main.movie.cast.MovieCast
import com.taufik.themovieshow.databinding.ItemCastBinding
import com.taufik.themovieshow.utils.LoadImage.loadImage

class MovieCastAdapter :
    ListAdapter<MovieCast, MovieCastAdapter.MovieViewHolder>(MovieCastDiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val itemsCastBinding =
            ItemCastBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MovieViewHolder(itemsCastBinding)
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class MovieViewHolder(private val binding: ItemCastBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: MovieCast) = with(binding) {
            imgPoster.loadImage(data.profilePath)
            tvCastName.text = data.name
        }
    }
}

object MovieCastDiffCallback : DiffUtil.ItemCallback<MovieCast>() {
    override fun areItemsTheSame(oldItem: MovieCast, newItem: MovieCast): Boolean = oldItem.id == newItem.id
    override fun areContentsTheSame(oldItem: MovieCast, newItem: MovieCast): Boolean = oldItem == newItem
}