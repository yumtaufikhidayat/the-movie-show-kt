package com.taufik.themovieshow.ui.main.tvshow.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.taufik.themovieshow.R
import com.taufik.themovieshow.data.main.tvshow.cast.TvShowsCast
import com.taufik.themovieshow.databinding.ItemCastBinding
import com.taufik.themovieshow.utils.loadImage

class TvShowsCastAdapter : ListAdapter<TvShowsCast, TvShowsCastAdapter.MovieViewHolder>(TvShowCastDiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val itemsCastBinding = ItemCastBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MovieViewHolder(itemsCastBinding)
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class MovieViewHolder(private val binding: ItemCastBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: TvShowsCast) = with(binding) {
            imgPoster.loadImage(data.profilePath)
            tvCastName.text = root.context.getString(R.string.tvNameCharacter, data.name, data.character)
        }
    }

    object TvShowCastDiffCallback : DiffUtil.ItemCallback<TvShowsCast>() {
        override fun areItemsTheSame(oldItem: TvShowsCast, newItem: TvShowsCast): Boolean = oldItem.id == newItem.id
        override fun areContentsTheSame(oldItem: TvShowsCast, newItem: TvShowsCast): Boolean = oldItem == newItem
    }
}