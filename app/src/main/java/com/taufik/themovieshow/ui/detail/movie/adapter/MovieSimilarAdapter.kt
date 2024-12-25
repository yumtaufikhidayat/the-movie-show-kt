package com.taufik.themovieshow.ui.detail.movie.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.taufik.themovieshow.databinding.ItemSimilarBinding
import com.taufik.themovieshow.model.response.movie.similar.MovieSimilarResult
import com.taufik.themovieshow.utils.CommonDateFormatConstants
import com.taufik.themovieshow.utils.extensions.convertDate
import com.taufik.themovieshow.utils.extensions.loadImage

class MovieSimilarAdapter(
    private val onItemClickListener: (MovieSimilarResult) -> Unit
) : ListAdapter<MovieSimilarResult, MovieSimilarAdapter.MovieViewHolder>(
    MOVIE_SIMILAR_DIFF_CALLBACK
) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        return MovieViewHolder(
            ItemSimilarBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class MovieViewHolder(private val binding: ItemSimilarBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: MovieSimilarResult) {
            binding.apply {
                imgPoster.loadImage(data.posterPath)
                val releaseYear = data.releaseDate.convertDate(
                    CommonDateFormatConstants.YYYY_MM_DD_FORMAT,
                    CommonDateFormatConstants.YYYY_FORMAT
                )
                tvMovieName.text = StringBuilder(data.originalTitle).append("\n").append("($releaseYear)")
                cardPoster.setOnClickListener {
                    onItemClickListener(data)
                }
            }
        }
    }

    companion object {
        val MOVIE_SIMILAR_DIFF_CALLBACK = object : DiffUtil.ItemCallback<MovieSimilarResult>() {
            override fun areItemsTheSame(
                oldItem: MovieSimilarResult,
                newItem: MovieSimilarResult
            ): Boolean = oldItem.id == newItem.id

            override fun areContentsTheSame(
                oldItem: MovieSimilarResult,
                newItem: MovieSimilarResult
            ): Boolean = oldItem == newItem
        }
    }
}