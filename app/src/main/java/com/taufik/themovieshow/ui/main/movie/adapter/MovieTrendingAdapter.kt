package com.taufik.themovieshow.ui.main.movie.adapter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.taufik.themovieshow.R
import com.taufik.themovieshow.data.main.movie.trending.MovieTrendingResult
import com.taufik.themovieshow.databinding.ItemsMoviesTvShowBinding
import com.taufik.themovieshow.ui.detail.movie.fragment.DetailMovieFragment
import com.taufik.themovieshow.utils.LoadImage.loadImage
import kotlin.math.roundToInt

class MovieTrendingAdapter : ListAdapter<MovieTrendingResult, MovieTrendingAdapter.MovieViewHolder>(MovieTrendingDiffCallback){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        return MovieViewHolder(ItemsMoviesTvShowBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class MovieViewHolder (private val binding: ItemsMoviesTvShowBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(data: MovieTrendingResult) = with(binding) {
            imgPoster.loadImage(data.posterPath)
            tvTitle.text = data.title
            tvReleaseDate.text = data.releaseDate

            val ratingOld = data.voteAverage
            val ratingNew = (ratingOld * 10.0).roundToInt() / 10.0
            tvRating.text = ratingNew.toString()

            itemView.setOnClickListener {
                val bundle = Bundle()
                bundle.putParcelable(DetailMovieFragment.EXTRA_DATA, data)
                it.findNavController().navigate(R.id.detailMovieFragment, bundle)
            }
        }
    }

    object MovieTrendingDiffCallback: DiffUtil.ItemCallback<MovieTrendingResult>(){
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