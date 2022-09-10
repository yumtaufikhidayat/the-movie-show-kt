package com.taufik.themovieshow.ui.main.tvshow.adapter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.taufik.themovieshow.R
import com.taufik.themovieshow.data.api.UrlEndpoint
import com.taufik.themovieshow.data.main.tvshow.popularairingtoday.TvShowsMainResult
import com.taufik.themovieshow.databinding.ItemsMoviesTvShowBinding
import com.taufik.themovieshow.ui.detail.DetailTvShowFragment
import com.taufik.themovieshow.utils.CommonFormatConstants
import com.taufik.themovieshow.utils.convertDate
import com.taufik.themovieshow.utils.loadImage
import com.taufik.themovieshow.utils.toRating

class TvShowsAdapter : ListAdapter<TvShowsMainResult, TvShowsAdapter.TvShowsViewHolder>(TvShowsCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TvShowsViewHolder {
        val itemsMovieShowBinding = ItemsMoviesTvShowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TvShowsViewHolder(itemsMovieShowBinding)
    }

    override fun onBindViewHolder(holder: TvShowsViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class TvShowsViewHolder(private val binding: ItemsMoviesTvShowBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(data: TvShowsMainResult) {
            with(binding) {
                imgPoster.loadImage(UrlEndpoint.IMAGE_URL + data.posterPath)
                tvTitle.text = data.name
                tvReleaseDate.text = data.firstAirDate.convertDate(
                    CommonFormatConstants.YYYY_MM_DD_FORMAT,
                    CommonFormatConstants.EEE_D_MMM_YYYY_FORMAT
                )
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

    object TvShowsCallback: DiffUtil.ItemCallback<TvShowsMainResult>() {
        override fun areItemsTheSame(
            oldItem: TvShowsMainResult,
            newItem: TvShowsMainResult
        ): Boolean = oldItem.id == newItem.id

        override fun areContentsTheSame(
            oldItem: TvShowsMainResult,
            newItem: TvShowsMainResult
        ): Boolean = oldItem == newItem
    }
}