package com.taufik.themovieshow.ui.main.movie.adapter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.taufik.themovieshow.R
import com.taufik.themovieshow.data.main.movie.discover.DiscoverMovieResult
import com.taufik.themovieshow.databinding.ItemsMoviesTvShowBinding
import com.taufik.themovieshow.ui.detail.movie.fragment.DetailMovieFragment
import com.taufik.themovieshow.utils.loadImage
import com.taufik.themovieshow.utils.toRating

class DiscoverMovieAdapter : RecyclerView.Adapter<DiscoverMovieAdapter.MovieViewHolder>(){

    private var listMovies = ArrayList<DiscoverMovieResult>()

    fun setMovies(movieResult: ArrayList<DiscoverMovieResult>) {
        this.listMovies.clear()
        this.listMovies.addAll(movieResult)
        notifyDataSetChanged()
    }

    inner class MovieViewHolder (private val binding: ItemsMoviesTvShowBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(data: DiscoverMovieResult) {
            with(binding) {
               imgPoster.loadImage(data.posterPath)
                tvTitle.text = data.title
                tvReleaseDate.text = data.releaseDate
                tvRating.text = toRating(data.voteAverage)

                itemView.setOnClickListener {
                    val bundle = Bundle()
                    bundle.putParcelable(DetailMovieFragment.EXTRA_DATA, data)
                    it.findNavController().navigate(R.id.detailMovieFragment, bundle)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val itemsMovieBinding = ItemsMoviesTvShowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MovieViewHolder(itemsMovieBinding)
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        val pos = listMovies[position]
        holder.bind(pos)
    }

    override fun getItemCount(): Int = listMovies.size
}