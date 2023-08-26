package com.taufik.themovieshow.ui.movie.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.taufik.themovieshow.databinding.ItemsMoviesTvShowBinding
import com.taufik.themovieshow.model.response.movie.trending.MovieTrendingResult
import com.taufik.themovieshow.utils.CommonDateFormatConstants
import com.taufik.themovieshow.utils.convertDate
import com.taufik.themovieshow.utils.loadImage
import com.taufik.themovieshow.utils.toRating

class MovieTrendingAdapter(
    private val onItemClickListener: (MovieTrendingResult) -> Unit
) : ListAdapter<MovieTrendingResult, MovieTrendingAdapter.ViewHolder>(MOVIE_TRENDING_DIFF_CALLBACK){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemsMoviesTvShowBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(getItem(position))

    inner class ViewHolder(private val binding: ItemsMoviesTvShowBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: MovieTrendingResult) {
            binding.apply {
                imgPoster.loadImage(data.posterPath)
                tvTitle.text = data.title
                tvReleaseDate.text = data.releaseDate.convertDate(
                    CommonDateFormatConstants.YYYY_MM_DD_FORMAT,
                    CommonDateFormatConstants.EEE_D_MMM_YYYY_FORMAT
                )
                tvRating.text = toRating(data.voteAverage)

                itemView.setOnClickListener {
                    onItemClickListener(data)
                }
            }
        }
    }

    companion object {
        val MOVIE_TRENDING_DIFF_CALLBACK = object : DiffUtil.ItemCallback<MovieTrendingResult>() {
            override fun areItemsTheSame(
                oldItem: MovieTrendingResult,
                newItem: MovieTrendingResult
            ): Boolean = oldItem == newItem

            override fun areContentsTheSame(
                oldItem: MovieTrendingResult,
                newItem: MovieTrendingResult
            ): Boolean = oldItem.id == newItem.id
        }
    }
}