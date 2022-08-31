package com.taufik.themovieshow.ui.detail.movie.fragment

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.taufik.themovieshow.R
import com.taufik.themovieshow.data.main.movie.nowplayingupcoming.MovieMainResult
import com.taufik.themovieshow.data.viewmodel.movie.DetailMovieViewModel
import com.taufik.themovieshow.databinding.FragmentDetailMovieFavoriteBinding
import com.taufik.themovieshow.ui.main.movie.adapter.MovieCastAdapter
import com.taufik.themovieshow.utils.loadImage
import com.taufik.themovieshow.utils.toRating
import es.dmoral.toasty.Toasty
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*

class DetailMovieFavoriteFragment : Fragment() {

    private var _binding: FragmentDetailMovieFavoriteBinding? = null
    private val binding get() = _binding!!

    private val viewModel: DetailMovieViewModel by viewModels()

    private var idMovie = 0
    private var title = ""
    private var isChecked = false

    private lateinit var castAdapter: MovieCastAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentDetailMovieFavoriteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getBundleData()
        showToolbarData()
        setData()
        setCastAdapter()
        setReadMore()
    }

    private fun getBundleData() {
        val data = arguments?.getParcelable<MovieMainResult>(EXTRA_DATA) as MovieMainResult
        idMovie = data.id
        title = data.title
    }

    private fun showToolbarData() = with(binding) {
        toolbarDetailMovieFavorite.imgBack.setOnClickListener {
            findNavController().popBackStack()
        }
        toolbarDetailMovieFavorite.tvToolbar.text = title
    }

    private fun setData() = with(binding) {
        viewModel.apply {
            setDetailMovies(idMovie)
            getDetailMovies().observe(viewLifecycleOwner) {
                if (it != null) {
                    imgPosterFavorite.loadImage(it.posterPath)
                    imgBackdropFavorite.loadImage(it.backdropPath)
                    tvTitleFavorite.text = it.title
                    tvReleaseDateFavorite.text = it.releaseDate
                    tvStatusFavorite.text = it.status
                    tvOverviewFavorite.text = it.overview
                    tvRatingFavorite.text = toRating(it.voteAverage)

                    when {
                        it.genres.isEmpty() -> tvGenreFavorite.text = "N/A"
                        else -> tvGenreFavorite.text = it.genres[0].name
                    }

                    tvRuntimeFavorite.text = String.format("${it.runtime} min")

                    checkFavoriteData(idMovie)
                    setActionFavorite(idMovie, it.posterPath, title, it.releaseDate, it.voteAverage)
                    shareMovie(it.homepage)
                    setCast(idMovie)
                    showVideo(idMovie)
                }
            }
        }
    }

    private fun checkFavoriteData(id: Int) = with(binding) {
        CoroutineScope(Dispatchers.IO).launch {
            val count = viewModel.checkFavorite(id)
            withContext(Dispatchers.Main) {
                if (count != null) {
                    if (count > 0) {
                        toolbarDetailMovieFavorite.toggleFavorite.isChecked = true
                        isChecked = true
                    } else {
                        toolbarDetailMovieFavorite.toggleFavorite.isChecked = false
                        isChecked = false
                    }
                }
            }
        }
    }

    private fun setActionFavorite(
        id: Int,
        posterPath: String,
        title: String,
        releaseDate: String,
        voteAverage: Double
    ) = with(binding) {
        toolbarDetailMovieFavorite.toggleFavorite.setOnClickListener {
            isChecked = !isChecked
            if (isChecked) {
                viewModel.addToFavorite(
                    id,
                    posterPath,
                    title,
                    releaseDate,
                    voteAverage
                )
                showToasty("Added to favorite")
            } else {
                viewModel.removeFromFavorite(id)
                showToasty("Removed from favorite")
            }
        }
    }

    private fun shareMovie(link: String) = with(binding) {
        toolbarDetailMovieFavorite.imgShare.setOnClickListener {
            try {
                val body = "Visit this awesome movie \n${link}"
                val shareIntent = Intent(Intent.ACTION_SEND)
                shareIntent.apply {
                    type = "text/plain"
                    putExtra(Intent.EXTRA_TEXT, body)
                }
                startActivity(Intent.createChooser(shareIntent, "Share with:"))
            } catch (e: Exception) {
                Log.e("shareFailed", "onOptionsItemSelected: ${e.localizedMessage}")
            }
        }
    }

    private fun showVideo(id: Int) = with(binding) {
        viewModel.apply {
            setDetailMovieVideo(id)
            getDetailMovieVideo().observe(viewLifecycleOwner) {
                if (it != null) {
                    when {
                        it.results.isEmpty() -> tvTrailerFavorite.text = String.format(Locale.getDefault(), "Trailer Video Not Available")
                        else -> tvTrailerFavorite.text = it.results[0].name
                    }

                    lifecycle.addObserver(videoTrailer)
                    videoTrailer.addYouTubePlayerListener(object : AbstractYouTubePlayerListener() {
                        override fun onReady(youTubePlayer: YouTubePlayer) {
                            when {
                                it.results.isEmpty() -> Log.e("videoFailed", "onReady: ")
                                else -> {
                                    val videoId = it.results[0].key
                                    youTubePlayer.loadVideo(videoId, 0F)
                                }
                            }
                        }
                    })
                }
            }
        }
    }

    private fun setCastAdapter() = with(binding) {
        castAdapter = MovieCastAdapter()
        rvMovieCastFavorite.apply {
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            setHasFixedSize(true)
            adapter = castAdapter
        }
    }

    private fun setCast(id: Int) {
        viewModel.apply {
            setDetailMovieCast(id)
            getDetailMovieCast().observe(viewLifecycleOwner) {
                if (it != null) {
                    castAdapter.submitList(it)
                }
            }
        }
    }

    private fun setReadMore() = with(binding) {
        tvReadMoreFavorite.isVisible = true
        tvReadMoreFavorite.setOnClickListener {
            if (tvReadMoreFavorite.text.toString() == "Read More") {
                tvOverviewFavorite.maxLines = Integer.MAX_VALUE
                tvOverviewFavorite.ellipsize = null
                tvReadMoreFavorite.text = getString(R.string.tvReadLess)
            } else {
                tvOverviewFavorite.maxLines = 4
                tvOverviewFavorite.ellipsize = TextUtils.TruncateAt.END
                tvReadMoreFavorite.text = getString(R.string.tvReadMore)
            }
        }
    }

    private fun showToasty(message: String) {
        Toasty.success(requireContext(), message, Toast.LENGTH_SHORT, true).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        const val EXTRA_DATA = "com.taufik.themovieshow.ui.detail.movie.fragment.EXTRA_DATA"
    }
}