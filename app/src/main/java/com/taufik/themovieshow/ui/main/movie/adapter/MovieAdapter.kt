package com.taufik.themovieshow.ui.main.movie.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.taufik.themovieshow.databinding.ItemsMoviesTvShowBinding
import com.taufik.themovieshow.ui.main.movie.activity.DetailMovieActivity
import com.taufik.themovieshow.data.main.movie.nowplayingupcoming.MovieMainResult
import com.taufik.themovieshow.utils.LoadImage.loadImage

class MovieAdapter : ListAdapter<MovieMainResult, MovieAdapter.MovieViewHolder>(MovieDiffCallback){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        return MovieViewHolder(ItemsMoviesTvShowBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class MovieViewHolder (private val binding: ItemsMoviesTvShowBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(data: MovieMainResult) {
            with(binding) {
                imgPoster.loadImage(data.posterPath)
                tvTitle.text = data.title
                tvReleaseDate.text = data.releaseDate
                tvRating.text = data.voteAverage.toString()

                itemView.setOnClickListener {
                    val intent = Intent(itemView.context, DetailMovieActivity::class.java).apply {
                        putExtra(DetailMovieActivity.EXTRA_DETAIL_ID, data.id)
                        putExtra(DetailMovieActivity.EXTRA_DETAIL_TITLE, data.title)
                    }
                    it.context.startActivity(intent)
                }
            }
        }
    }

    object MovieDiffCallback : DiffUtil.ItemCallback<MovieMainResult>(){
        override fun areItemsTheSame(oldItem: MovieMainResult, newItem: MovieMainResult): Boolean = oldItem.id == newItem.id
        override fun areContentsTheSame(oldItem: MovieMainResult, newItem: MovieMainResult): Boolean = oldItem == newItem
    }
}