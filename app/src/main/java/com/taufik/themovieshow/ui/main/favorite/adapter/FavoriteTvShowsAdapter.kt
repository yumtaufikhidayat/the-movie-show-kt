package com.taufik.themovieshow.ui.main.favorite.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.taufik.themovieshow.R
import com.taufik.themovieshow.data.remote.api.UrlEndpoint
import com.taufik.themovieshow.databinding.ItemsMoviesTvShowBinding
import com.taufik.themovieshow.model.response.tvshow.popularairingtoday.TvShowsMainResult
import com.taufik.themovieshow.utils.CommonDateFormatConstants
import com.taufik.themovieshow.utils.convertDate
import com.taufik.themovieshow.utils.loadImage
import com.taufik.themovieshow.utils.toRating
import java.util.Locale

class FavoriteTvShowsAdapter(
    private val onItemClickListener: (TvShowsMainResult) -> Unit
) : ListAdapter<TvShowsMainResult, FavoriteTvShowsAdapter.TvShowsViewHolder>(
    favoriteTvShowsCallback
), Filterable {

    private var listTvShows =
        listOf<TvShowsMainResult>()

    fun setData(list: List<TvShowsMainResult>) {
        this.listTvShows = list
        submitList(list)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TvShowsViewHolder {
        val itemsMovieShowBinding =
            ItemsMoviesTvShowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TvShowsViewHolder(itemsMovieShowBinding)
    }

    override fun onBindViewHolder(holder: TvShowsViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class TvShowsViewHolder(private val binding: ItemsMoviesTvShowBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: TvShowsMainResult) {
            with(binding) {
                imgPoster.loadImage(UrlEndpoint.IMAGE_URL + data.posterPath)
                tvTitle.text = data.name
                tvReleaseDate.text = data.firstAirDate?.convertDate(
                    CommonDateFormatConstants.YYYY_MM_DD_FORMAT,
                    CommonDateFormatConstants.EEE_D_MMM_YYYY_FORMAT
                ) ?: root.context.getString(R.string.tvNA)
                tvRating.text = toRating(data.voteAverage)

                cardMoviesTvShow.setOnClickListener {
                    onItemClickListener(data)
                }
            }
        }
    }

    override fun getFilter(): Filter = searchFilter

    private val searchFilter = object : Filter() {
        override fun performFiltering(p0: CharSequence): FilterResults {
            val filteredList =
                mutableListOf<TvShowsMainResult>()
            if (p0.isEmpty()) {
                filteredList.addAll(listTvShows)
            } else {
                val filterPattern = p0.toString().lowercase(Locale.ROOT).trim()
                listTvShows.forEach { item ->
                    if (item.name.lowercase().contains(filterPattern)) filteredList.add(item)
                }
            }

            val result = FilterResults()
            result.values = filteredList
            return result
        }

        override fun publishResults(p0: CharSequence?, p1: FilterResults?) {
            submitList(p1?.values as MutableList<TvShowsMainResult>?)
        }
    }

    companion object {
        val favoriteTvShowsCallback = object : DiffUtil.ItemCallback<TvShowsMainResult>() {
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
}