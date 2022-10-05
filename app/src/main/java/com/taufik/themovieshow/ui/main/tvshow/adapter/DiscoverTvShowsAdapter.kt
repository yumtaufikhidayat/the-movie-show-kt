package com.taufik.themovieshow.ui.main.tvshow.adapter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.taufik.themovieshow.R
import com.taufik.themovieshow.data.main.tvshow.discover.DiscoverTvShowsResult
import com.taufik.themovieshow.databinding.ItemsMoviesTvShowBinding
import com.taufik.themovieshow.ui.detail.tvshow.fragment.DetailTvShowFragment
import com.taufik.themovieshow.utils.CommonDateFormatConstants.EEE_D_MMM_YYYY_FORMAT
import com.taufik.themovieshow.utils.CommonDateFormatConstants.YYYY_MM_DD_FORMAT
import com.taufik.themovieshow.utils.convertDate
import com.taufik.themovieshow.utils.loadImage
import com.taufik.themovieshow.utils.toRating

class DiscoverTvShowsAdapter : ListAdapter<DiscoverTvShowsResult, DiscoverTvShowsAdapter.TvShowsViewHolder>(DiscoverTvShowCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TvShowsViewHolder {
        val itemsMovieShowBinding = ItemsMoviesTvShowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TvShowsViewHolder(itemsMovieShowBinding)
    }

    override fun onBindViewHolder(holder: TvShowsViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class TvShowsViewHolder(private val binding: ItemsMoviesTvShowBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(data: DiscoverTvShowsResult) {
            with(binding) {
                imgPoster.loadImage(data.posterPath)
                tvTitle.text = data.name
                tvReleaseDate.text = data.firstAirDate.convertDate(YYYY_MM_DD_FORMAT, EEE_D_MMM_YYYY_FORMAT)
                tvRating.text = toRating(data.voteAverage)

                itemView.setOnClickListener {
                    val bundle = Bundle()
                    bundle.putInt(DetailTvShowFragment.EXTRA_ID, data.id)
                    bundle.putString(DetailTvShowFragment.EXTRA_TITLE, data.name)
                    it.findNavController().navigate(R.id.detailTvShowFragment, bundle)
                }
            }
        }
    }

    object DiscoverTvShowCallback: DiffUtil.ItemCallback<DiscoverTvShowsResult>(){
        override fun areItemsTheSame(
            oldItem: DiscoverTvShowsResult,
            newItem: DiscoverTvShowsResult
        ): Boolean = oldItem.id == newItem.id

        override fun areContentsTheSame(
            oldItem: DiscoverTvShowsResult,
            newItem: DiscoverTvShowsResult
        ): Boolean = oldItem == newItem
    }
}