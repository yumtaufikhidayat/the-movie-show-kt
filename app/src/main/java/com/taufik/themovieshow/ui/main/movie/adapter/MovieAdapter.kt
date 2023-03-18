package com.taufik.themovieshow.ui.main.movie.adapter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.taufik.themovieshow.R
import com.taufik.themovieshow.model.response.movie.nowplayingupcoming.MovieMainResult
import com.taufik.themovieshow.databinding.ItemsMoviesTvShowBinding
import com.taufik.themovieshow.ui.detail.movie.fragment.DetailMovieFragment
import com.taufik.themovieshow.utils.CommonDateFormatConstants
import com.taufik.themovieshow.utils.convertDate
import com.taufik.themovieshow.utils.loadImage
import com.taufik.themovieshow.utils.toRating
import java.util.*

class MovieAdapter : ListAdapter<com.taufik.themovieshow.model.response.movie.nowplayingupcoming.MovieMainResult, MovieAdapter.MovieViewHolder>(MovieDiffCallback), Filterable{

    private var listMovies = listOf<com.taufik.themovieshow.model.response.movie.nowplayingupcoming.MovieMainResult>()

    fun setData(list: List<com.taufik.themovieshow.model.response.movie.nowplayingupcoming.MovieMainResult>) {
        this.listMovies = list
        submitList(list)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        return MovieViewHolder(ItemsMoviesTvShowBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class MovieViewHolder (private val binding: ItemsMoviesTvShowBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(data: com.taufik.themovieshow.model.response.movie.nowplayingupcoming.MovieMainResult) {
            with(binding) {
                imgPoster.loadImage(data.posterPath)
                tvTitle.text = data.title
                tvReleaseDate.text = data.releaseDate?.convertDate(
                    CommonDateFormatConstants.YYYY_MM_DD_FORMAT,
                    CommonDateFormatConstants.EEE_D_MMM_YYYY_FORMAT
                ) ?: root.context.getString(R.string.tvNA)
                tvRating.text = toRating(data.voteAverage)

                itemView.setOnClickListener {
                    val bundle = Bundle()
                    bundle.putInt(DetailMovieFragment.EXTRA_ID, data.id)
                    bundle.putString(DetailMovieFragment.EXTRA_TITLE, data.title)
                    it.findNavController().navigate(R.id.detailMovieFragment, bundle)
                }
            }
        }
    }

    object MovieDiffCallback : DiffUtil.ItemCallback<com.taufik.themovieshow.model.response.movie.nowplayingupcoming.MovieMainResult>(){
        override fun areItemsTheSame(oldItem: com.taufik.themovieshow.model.response.movie.nowplayingupcoming.MovieMainResult, newItem: com.taufik.themovieshow.model.response.movie.nowplayingupcoming.MovieMainResult): Boolean = oldItem.id == newItem.id
        override fun areContentsTheSame(oldItem: com.taufik.themovieshow.model.response.movie.nowplayingupcoming.MovieMainResult, newItem: com.taufik.themovieshow.model.response.movie.nowplayingupcoming.MovieMainResult): Boolean = oldItem == newItem
    }

    override fun getFilter(): Filter = searchFilter

    private val searchFilter = object : Filter() {
        override fun performFiltering(p0: CharSequence): FilterResults {
            val filteredList = mutableListOf<com.taufik.themovieshow.model.response.movie.nowplayingupcoming.MovieMainResult>()
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
            submitList(p1?.values as MutableList<com.taufik.themovieshow.model.response.movie.nowplayingupcoming.MovieMainResult>?)
        }
    }
}