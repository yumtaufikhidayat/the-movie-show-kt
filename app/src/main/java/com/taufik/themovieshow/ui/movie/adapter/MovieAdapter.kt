package com.taufik.themovieshow.ui.movie.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.taufik.themovieshow.R
import com.taufik.themovieshow.api.UrlEndpoint
import com.taufik.themovieshow.databinding.ItemsMoviesTvShowBinding
import com.taufik.themovieshow.ui.movie.activity.DetailMovieActivity
import com.taufik.themovieshow.ui.movie.model.nowplayingupcoming.MovieMainResult

class MovieAdapter : ListAdapter<MovieMainResult, MovieAdapter.MovieViewHolder>(MovieDiffCallback){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        return MovieViewHolder(ItemsMoviesTvShowBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class MovieViewHolder (private val binding: ItemsMoviesTvShowBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(movieResult: MovieMainResult) {
            with(binding) {
                Glide.with(itemView.context)
                    .load(UrlEndpoint.IMAGE_URL + movieResult.posterPath)
                    .apply(
                        RequestOptions.placeholderOf(R.drawable.ic_loading)
                            .error(R.drawable.ic_error)
                    )
                    .into(imgPoster)

                tvTitle.text = movieResult.title
                tvReleaseDate.text = movieResult.releaseDate
                tvRating.text = movieResult.voteAverage.toString()

                itemView.setOnClickListener {
                    val intent = Intent(itemView.context, DetailMovieActivity::class.java).apply {
                        putExtra(DetailMovieActivity.EXTRA_DETAIL_ID, movieResult.id)
                        putExtra(DetailMovieActivity.EXTRA_DETAIL_TITLE, movieResult.title)
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