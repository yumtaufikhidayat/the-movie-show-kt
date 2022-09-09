package com.taufik.themovieshow.ui.detail

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
import com.taufik.themovieshow.data.viewmodel.tvshow.DetailTvShowViewModel
import com.taufik.themovieshow.databinding.FragmentDetailTvShowBinding
import com.taufik.themovieshow.ui.main.tvshow.adapter.TvShowsCastAdapter
import com.taufik.themovieshow.utils.CommonConstants
import com.taufik.themovieshow.utils.convertDate
import com.taufik.themovieshow.utils.loadImage
import com.taufik.themovieshow.utils.toRating
import es.dmoral.toasty.Toasty
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*

class DetailTvShowFragment : Fragment() {

    private var _binding: FragmentDetailTvShowBinding? = null
    private val binding get() = _binding!!

    private val viewModel: DetailTvShowViewModel by viewModels()
    private val castAdapter by lazy { TvShowsCastAdapter() }

    private var idTvShow = 0
    private var title = ""
    private var isChecked = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentDetailTvShowBinding.inflate(inflater, container, false)
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
        val bundle = this.arguments
        if (bundle != null) {
            idTvShow = bundle.getInt(DetailMovieFragment.EXTRA_ID, 0)
            title = bundle.getString(DetailMovieFragment.EXTRA_TITLE, "")
        }
    }

    private fun showToolbarData() = with(binding) {
        toolbarDetailTvShow.apply {
            tvToolbar.text = title
            imgBack.setOnClickListener {
                findNavController().popBackStack()
            }
        }
    }

    private fun setData() = with(binding){
        viewModel.apply {
            setDetailTvShowPopular(idTvShow)
            detailTvShows.observe(viewLifecycleOwner) {
                if (it != null) {
                    imgPoster.loadImage(it.posterPath)
                    imgBackdrop.loadImage(it.backdropPath)
                    tvTitle.text = it.name

                    when {
                        it.networks.isEmpty() -> tvNetwork.text = "(N/A)"
                        it.networks[0].originCountry == "" -> tvNetwork.text = String.format("${it.networks[0].name} (N/A)")
                        else -> tvNetwork.text = String.format("${it.networks[0].name} (${it.networks[0].originCountry})")
                    }

                    tvReleaseDate.text = it.firstAirDate.convertDate(
                        CommonConstants.YYYY_MM_DD_FORMAT,
                        CommonConstants.EEE_D_MMM_YYYY_FORMAT
                    )
                    tvStatus.text = it.status
                    tvOverview.text = it.overview
                    tvRating.text = toRating(it.voteAverage)

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
                    showVideo(idTvShow)
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
                        toolbarDetailTvShow.toggleFavorite.isChecked = true
                        isChecked = true
                    } else {
                        toolbarDetailTvShow.toggleFavorite.isChecked = false
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
        toolbarDetailTvShow.toggleFavorite.setOnClickListener {
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
                showToasty("Removed from favorite")
            }
        }
    }

    private fun shareTvShow(link: String) = with(binding) {
        toolbarDetailTvShow.imgShare.setOnClickListener {
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

    private fun showVideo(id: Int) = with(binding) {
        viewModel.apply {
            setDetailTvShowVideo(id)
            detailVideo.observe(viewLifecycleOwner) {
                if (it != null) {
                    when {
                        it.results.isEmpty() -> tvTrailer.text = String.format(Locale.getDefault(), "Trailer Video Not Available")
                        else -> tvTrailer.text = it.results[0].name
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
        rvTvShowCast.apply {
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            setHasFixedSize(true)
            adapter = castAdapter
        }
    }

    private fun setCast(id: Int) {
        viewModel.apply {
            setDetailTvShowsCast(id)
            listDetailCasts.observe(viewLifecycleOwner) {
                if (it != null) {
                    castAdapter.submitList(it)
                }
            }
        }
    }

    private fun setReadMore() = with(binding) {
        tvReadMore.isVisible = true
        tvReadMore.setOnClickListener {
            if (tvReadMore.text.toString() == "Read More") {
                tvOverview.maxLines = Integer.MAX_VALUE
                tvOverview.ellipsize = null
                tvReadMore.text = getString(R.string.tvReadLess)
            } else {
                tvOverview.maxLines = 4
                tvOverview.ellipsize = TextUtils.TruncateAt.END
                tvReadMore.text = getString(R.string.tvReadMore)
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
        const val EXTRA_ID = "EXTRA_ID"
        const val EXTRA_TITLE = "EXTRA_TITLE"
    }
}