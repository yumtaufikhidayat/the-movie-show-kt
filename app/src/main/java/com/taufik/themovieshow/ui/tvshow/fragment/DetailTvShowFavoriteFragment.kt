package com.taufik.themovieshow.ui.tvshow.fragment

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
import com.taufik.themovieshow.databinding.FragmentDetailTvShowFavoriteBinding
import com.taufik.themovieshow.ui.tvshow.adapter.TvShowsCastAdapter
import com.taufik.themovieshow.ui.tvshow.model.popularairingtoday.TvShowsMainResult
import com.taufik.themovieshow.ui.tvshow.viewmodel.DetailTvShowViewModel
import com.taufik.themovieshow.utils.LoadImage.loadImage
import es.dmoral.toasty.Toasty
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*

class DetailTvShowFavoriteFragment : Fragment() {

    private var _binding: FragmentDetailTvShowFavoriteBinding? = null
    private val binding get() = _binding!!

    private val viewModel: DetailTvShowViewModel by viewModels()

    private var idTvShow = 0
    private var title = ""
    private var isChecked = false

    private lateinit var castAdapter: TvShowsCastAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentDetailTvShowFavoriteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getBundleData()
        showToolbarData()
        showData()
        showVideo()
        setCastAdapter()
        setReadMore()
    }

    private fun getBundleData() {
        val tvShowsTrendingResult = arguments?.getParcelable<TvShowsMainResult>(DetailTvShowFragment.EXTRA_DATA) as TvShowsMainResult
        idTvShow = tvShowsTrendingResult.id
        title = tvShowsTrendingResult.name
    }

    private fun showToolbarData() = with(binding) {
        toolbarDetailTvShowFavorite.toolbarDetailTvShow.setOnClickListener {
            findNavController().popBackStack()
        }
        toolbarDetailTvShowFavorite.tvToolbar.text = title
    }

    private fun showData() = with(binding){
        viewModel.apply {
            setDetailTvShowPopular(idTvShow)
            getDetailTvShowsPopular().observe(viewLifecycleOwner) {
                if (it != null) {
                    imgPosterFavorite.loadImage(it.posterPath)
                    imgBackdropFavorite.loadImage(it.backdropPath)
                    tvTitleFavorite.text = it.name

                    when {
                        it.networks.isEmpty() -> tvNetworkFavorite.text = "(N/A)"
                        it.networks[0].originCountry == "" -> tvNetworkFavorite.text = String.format("${it.networks[0].name} (N/A)")
                        else -> tvNetworkFavorite.text = String.format("${it.networks[0].name} (${it.networks[0].originCountry})")
                    }

                    tvReleaseDateFavorite.text = it.firstAirDate
                    tvStatusFavorite.text = it.status
                    tvOverviewFavorite.text = it.overview
                    tvRating.text = it.voteAverage.toString()

                    when {
                        it.genres.isEmpty() -> tvGenre.text = "N/A"
                        else -> tvGenre.text = it.genres[0].name
                    }

                    when {
                        it.episodeRunTime.isEmpty() -> tvEpisodes.text = "N/A"
                        else -> tvEpisodes.text = String.format("${it.episodeRunTime[0]} episodes")
                    }

                    checkFavoriteData(idTvShow)
                    setActionFavorite(idTvShow, it.posterPath, title, it.firstAirDate, it.voteAverage)
                    shareTvShow(it.homepage)
                    setCast(idTvShow)
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
                        toolbarDetailTvShowFavorite.toggleFavorite.isChecked = true
                        isChecked = true
                    } else {
                        toolbarDetailTvShowFavorite.toggleFavorite.isChecked = false
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
        firstAirDate: String,
        voteAverage: Double
    ) = with(binding) {
        toolbarDetailTvShowFavorite.toggleFavorite.setOnClickListener {
            isChecked = !isChecked
            if (isChecked) {
                viewModel.addToFavorite(
                    id,
                    posterPath,
                    title,
                    firstAirDate,
                    voteAverage
                )
                showToasty("Added to favorite")
            } else {
                viewModel.removeFromFavorite(id)
                showToasty("Removed from favrite")
            }
        }
    }

    private fun shareTvShow(link: String) = with(binding) {
        toolbarDetailTvShowFavorite.imgShare.setOnClickListener {
            try {
                val body = "Visit this awesome shows \n${link}"
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

    private fun showVideo() = with(binding) {
        viewModel.apply {
            setDetailTvShowVideo(id)
            getDetailTvShowsVideo().observe(viewLifecycleOwner) {
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
        castAdapter = TvShowsCastAdapter()
        rvTvShowCastFavorite.apply {
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            setHasFixedSize(true)
            adapter = castAdapter
        }
    }

    private fun setCast(id: Int) {
        viewModel.apply {
            setDetailTvShowsCast(id)
            getDetailTvShowsCast().observe(viewLifecycleOwner) {
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
        const val EXTRA_DATA = "com.taufik.themovieshow.ui.tvshow.fragment.EXTRA_DATA"
    }
}