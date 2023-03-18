package com.taufik.themovieshow.ui.detail.tvshow.adapter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.taufik.themovieshow.R
import com.taufik.themovieshow.model.response.tvshow.similar.TvShowsSimilarResultsItem
import com.taufik.themovieshow.databinding.ItemSimilarBinding
import com.taufik.themovieshow.ui.detail.movie.fragment.DetailMovieFragment
import com.taufik.themovieshow.utils.CommonDateFormatConstants
import com.taufik.themovieshow.utils.convertDate
import com.taufik.themovieshow.utils.loadImage

class TvShowSimilarAdapter: ListAdapter<com.taufik.themovieshow.model.response.tvshow.similar.TvShowsSimilarResultsItem, TvShowSimilarAdapter.TvShowViewHolder>(
    TvShowSimilarDiffCallback
) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TvShowViewHolder {
        return TvShowViewHolder(ItemSimilarBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: TvShowViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class TvShowViewHolder(private val binding: ItemSimilarBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: com.taufik.themovieshow.model.response.tvshow.similar.TvShowsSimilarResultsItem) = with(binding) {
            imgPoster.loadImage(data.posterPath)
            val releaseYear = data.firstAirDate.convertDate(CommonDateFormatConstants.YYYY_MM_DD_FORMAT, CommonDateFormatConstants.YYYY_FORMAT)
            tvMovieName.text = StringBuilder(data.name).append("\n").append("($releaseYear)")
            cardPoster.setOnClickListener {
                val bundle = Bundle()
                bundle.putInt(DetailMovieFragment.EXTRA_ID, data.id)
                bundle.putString(DetailMovieFragment.EXTRA_TITLE, data.name)
                it.findNavController().navigate(R.id.detailTvShowFragment, bundle)
            }
        }
    }

    object TvShowSimilarDiffCallback: DiffUtil.ItemCallback<com.taufik.themovieshow.model.response.tvshow.similar.TvShowsSimilarResultsItem>() {
        override fun areItemsTheSame(
            oldItem: com.taufik.themovieshow.model.response.tvshow.similar.TvShowsSimilarResultsItem,
            newItem: com.taufik.themovieshow.model.response.tvshow.similar.TvShowsSimilarResultsItem
        ): Boolean = oldItem.id == newItem.id

        override fun areContentsTheSame(
            oldItem: com.taufik.themovieshow.model.response.tvshow.similar.TvShowsSimilarResultsItem,
            newItem: com.taufik.themovieshow.model.response.tvshow.similar.TvShowsSimilarResultsItem
        ): Boolean = oldItem == newItem
    }
}