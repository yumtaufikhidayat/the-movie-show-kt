package com.taufik.themovieshow.ui.main.movie.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.taufik.themovieshow.R
import com.taufik.themovieshow.databinding.ItemsMoviesTvShowBinding
import com.taufik.themovieshow.model.response.movie.nowplayingupcoming.MovieMainResult
import com.taufik.themovieshow.utils.CommonDateFormatConstants
import com.taufik.themovieshow.utils.convertDate
import com.taufik.themovieshow.utils.loadImage
import com.taufik.themovieshow.utils.toRating

class MovieAdapter(
    private val onItemClickListener: (MovieMainResult) -> Unit
) : PagingDataAdapter<MovieMainResult, MovieAdapter.MovieViewHolder>(MovieDiffCallback) {

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
        fun bind(data: MovieMainResult) {
            binding.apply {
                imgPoster.loadImage(data.posterPath)
                tvTitle.text = data.title
                tvReleaseDate.text = data.releaseDate?.convertDate(
                    CommonDateFormatConstants.YYYY_MM_DD_FORMAT,
                    CommonDateFormatConstants.EEE_D_MMM_YYYY_FORMAT
                ) ?: root.context.getString(R.string.tvNA)
                tvRating.text = toRating(data.voteAverage)

                itemView.setOnClickListener {
                    onItemClickListener(data)
                }
            }
        }
    }

    object MovieDiffCallback :
        DiffUtil.ItemCallback<MovieMainResult>() {
        override fun areItemsTheSame(
            oldItem: MovieMainResult,
            newItem: MovieMainResult
        ): Boolean = oldItem.id == newItem.id

        override fun areContentsTheSame(
            oldItem: MovieMainResult,
            newItem: MovieMainResult
        ): Boolean = oldItem == newItem
    }
}