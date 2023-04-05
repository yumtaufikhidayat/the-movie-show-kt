package com.taufik.themovieshow.ui.main.movie.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.taufik.themovieshow.databinding.ItemsMoviesTvShowBinding
import com.taufik.themovieshow.model.response.movie.trending.MovieTrendingResult
import com.taufik.themovieshow.utils.CommonDateFormatConstants
import com.taufik.themovieshow.utils.convertDate
import com.taufik.themovieshow.utils.loadImage
import com.taufik.themovieshow.utils.toRating

class MovieTrendingAdapter(
    private val onItemClickListener: (MovieTrendingResult) -> Unit
) : PagingDataAdapter<MovieTrendingResult, MovieTrendingAdapter.MovieViewHolder>(movieTrendingDiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        return MovieViewHolder(
            ItemsMoviesTvShowBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        val data = getItem(position)
        if (data != null) holder.bind(data)
        holder.setIsRecyclable(false)
    }

    inner class MovieViewHolder(private val binding: ItemsMoviesTvShowBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: MovieTrendingResult) =
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

    companion object {
        val movieTrendingDiffCallback = object: DiffUtil.ItemCallback<MovieTrendingResult>() {
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