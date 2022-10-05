package com.taufik.themovieshow.ui.main.movie.adapter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.taufik.themovieshow.R
import com.taufik.themovieshow.data.main.movie.similar.MovieSimilarResult
import com.taufik.themovieshow.databinding.ItemSimilarBinding
import com.taufik.themovieshow.ui.detail.movie.fragment.DetailMovieFragment
import com.taufik.themovieshow.utils.CommonDateFormatConstants
import com.taufik.themovieshow.utils.convertDate
import com.taufik.themovieshow.utils.loadImage

class MovieSimilarAdapter: ListAdapter<MovieSimilarResult, MovieSimilarAdapter.MovieViewHolder>(MovieSimilarDiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        return MovieViewHolder(ItemSimilarBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class MovieViewHolder(private val binding: ItemSimilarBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: MovieSimilarResult) = with(binding) {
            imgPoster.loadImage(data.posterPath)
            val releaseYear = data.releaseDate.convertDate(CommonDateFormatConstants.YYYY_MM_DD_FORMAT, CommonDateFormatConstants.YYYY_FORMAT)
            tvMovieName.text = StringBuilder(data.originalTitle).append("\n").append("($releaseYear)")
            cardPoster.setOnClickListener {
                val bundle = Bundle()
                bundle.putInt(DetailMovieFragment.EXTRA_ID, data.id)
                bundle.putString(DetailMovieFragment.EXTRA_TITLE, data.title)
                it.findNavController().navigate(R.id.detailMovieFragment, bundle)
            }
        }
    }

    object MovieSimilarDiffCallback: DiffUtil.ItemCallback<MovieSimilarResult>() {
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