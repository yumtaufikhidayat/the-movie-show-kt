package com.taufik.themovieshow.ui.movie.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.taufik.themovieshow.R
import com.taufik.themovieshow.api.UrlEndpoint
import com.taufik.themovieshow.databinding.ItemCastBinding
import com.taufik.themovieshow.ui.movie.model.cast.MovieCast

class MovieCastAdapter : ListAdapter<MovieCast, MovieCastAdapter.MovieViewHolder>(MovieCastDiffCallback){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val itemsCastBinding = ItemCastBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MovieViewHolder(itemsCastBinding)
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class MovieViewHolder (private val binding: ItemCastBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(movieCast: MovieCast) {
            with(binding) {
                Glide.with(itemView.context)
                    .load(UrlEndpoint.IMAGE_URL + movieCast.profilePath)
                    .apply(
                        RequestOptions.placeholderOf(R.drawable.ic_loading)
                            .error(R.drawable.ic_error)
                    )
                    .into(imgPoster)

                tvCastName.text = movieCast.name
            }
        }
    }

    object MovieCastDiffCallback: DiffUtil.ItemCallback<MovieCast>() {
        override fun areItemsTheSame(oldItem: MovieCast, newItem: MovieCast): Boolean = oldItem.id == newItem.id
        override fun areContentsTheSame(oldItem: MovieCast, newItem: MovieCast): Boolean = oldItem == newItem
    }
}