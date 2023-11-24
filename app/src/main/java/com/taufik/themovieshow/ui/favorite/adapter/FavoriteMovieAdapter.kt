package com.taufik.themovieshow.ui.favorite.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.taufik.themovieshow.R
import com.taufik.themovieshow.databinding.ItemsMoviesTvShowBinding
import com.taufik.themovieshow.model.response.movie.nowplayingupcoming.MovieMainResult
import com.taufik.themovieshow.utils.CommonDateFormatConstants
import com.taufik.themovieshow.utils.convertDate
import com.taufik.themovieshow.utils.loadImage
import com.taufik.themovieshow.utils.toRating
import java.util.Locale

class FavoriteMovieAdapter(
    private val onItemClickListener: (MovieMainResult) -> Unit
) : ListAdapter<MovieMainResult, FavoriteMovieAdapter.MovieViewHolder>(
    FAVORITE_MOVIE_DIFF_CALLBACK
), Filterable {

    private var listMovies = listOf<MovieMainResult>()
    private var position: Int = 0

    fun setData(list: List<MovieMainResult>, position: Int) {
        this.listMovies = list
        this.position = position
        submitList(list)
    }

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
        holder.bind(getItem(position))
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
                mutableListOf<MovieMainResult>()
            if (p0.isEmpty()) {
                filteredList.addAll(listMovies)
            } else {
                val filterPattern = p0.toString().lowercase(Locale.ROOT).trim()
                listMovies.forEach { item ->
                    if (item.title.lowercase().contains(filterPattern)) filteredList.add(item)
                }
            }

            val result = FilterResults()
            result.values = filteredList
            return result
        }

        override fun publishResults(p0: CharSequence?, p1: FilterResults?) {
            submitList(p1?.values as MutableList<MovieMainResult>?)
        }
    }

    companion object {
        val FAVORITE_MOVIE_DIFF_CALLBACK = object : DiffUtil.ItemCallback<MovieMainResult>() {
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
}