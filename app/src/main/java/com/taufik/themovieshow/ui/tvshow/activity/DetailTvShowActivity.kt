package com.taufik.themovieshow.ui.tvshow.activity

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.taufik.themovieshow.R
import com.taufik.themovieshow.api.UrlEndpoint
import com.taufik.themovieshow.databinding.ActivityDetailTvShowBinding
import com.taufik.themovieshow.ui.tvshow.adapter.TvShowsCastAdapter
import com.taufik.themovieshow.ui.tvshow.model.detail.TvShowsPopularDetailResponse
import com.taufik.themovieshow.ui.tvshow.model.discover.DiscoverTvShowsResult
import com.taufik.themovieshow.ui.tvshow.viewmodel.DetailTvShowViewModel
import es.dmoral.toasty.Toasty
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*
import kotlin.properties.Delegates

class DetailTvShowActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailTvShowBinding
    private lateinit var detailTvShowViewModel: DetailTvShowViewModel
    private var id by Delegates.notNull<Int>()
    private lateinit var title: String
    private lateinit var data: TvShowsPopularDetailResponse
    private lateinit var tvShowResult: DiscoverTvShowsResult
    private lateinit var castAdapter: TvShowsCastAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailTvShowBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setParcelableData()

        initActionBar()

        setData()

        setVideo()

        setCastAdapter()

        setRecyclerView()

        setCast()

        setReadMore()
    }

    private fun setParcelableData() {
        tvShowResult = intent.getParcelableExtra<DiscoverTvShowsResult>(EXTRA_DETAIL_TV) as DiscoverTvShowsResult
    }

    private fun initActionBar() {
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setIcon(R.drawable.ic_arrow_back)
            title = tvShowResult.name
            elevation = 0F
        }
    }

    private fun setData() {
        detailTvShowViewModel = ViewModelProvider(this)[DetailTvShowViewModel::class.java]
//        detailTvShowViewModel.setDetailTvShowPopular(id, BuildConfig.API_KEY)
        detailTvShowViewModel.getDetailTvShowsPopular().observe(this) {
            data = it
            if (it != null) {
                binding.apply {
                    imgPoster.loadImage(it.posterPath)
                    imgBackdrop.loadImage(it.backdropPath)
                    tvTitle.text = it.name

                    when {
                        it.networks.isEmpty() -> {
                            tvNetwork.text = "(N/A)"
                        }
                        it.networks[0].originCountry == "" -> {
                            tvNetwork.text = String.format("${it.networks[0].name} (N/A)")
                        }
                        else -> {
                            tvNetwork.text =
                                String.format("${it.networks[0].name} (${it.networks[0].originCountry})")
                        }
                    }

                    tvReleaseDate.text = it.firstAirDate
                    tvStatus.text = it.status
                    tvOverview.text = it.overview
                    tvRating.text = it.voteAverage.toString()

                    when {
                        it.genres.isEmpty() -> {
                            tvGenre.text = "N/A"
                        }

                        else -> {
                            tvGenre.text = it.genres[0].name
                        }
                    }

                    when {
                        it.episodeRunTime.isEmpty() -> {
                            tvEpisodes.text = "N/A"
                        }

                        else -> {
                            tvEpisodes.text = String.format("${it.episodeRunTime[0]} episodes")
                        }
                    }

                    tvLanguage.text = it.originalLanguage
                }
            }
        }

        var isChecked = false
        binding.apply {
            CoroutineScope(Dispatchers.IO).launch {
                val count = detailTvShowViewModel.checkFavorite(id)
                withContext(Dispatchers.Main){
                    if (count != null) {
                        if (count > 0) {
                            toggleFavorite.isChecked = true
                            isChecked = true
                        } else {
                            toggleFavorite.isChecked = false
                            isChecked = false
                        }
                    }
                }
            }
        }

        binding.apply {
            tvFavorite.visibility = View.VISIBLE
            toggleFavorite.setOnClickListener {
                if(this@DetailTvShowActivity::data.isInitialized){
                    isChecked = !isChecked
                    if (isChecked) {
                        detailTvShowViewModel.addToFavorite(
                            id,
                            data.posterPath,
                            title,
                            data.firstAirDate,
                            data.voteAverage
                        )
                        Toasty.success(this@DetailTvShowActivity, "Added to favorite", Toast.LENGTH_SHORT, true).show()
                    } else {
                        detailTvShowViewModel.removeFromFavorite(id)
                        Toasty.success(this@DetailTvShowActivity, "Removed from favorite", Toast.LENGTH_SHORT, true).show()
                    }
                }
                binding.toggleFavorite.isChecked = isChecked
            }
        }
    }

    private fun setVideo() {
//        detailTvShowViewModel.setDetailTvShowVideo(id, BuildConfig.API_KEY)
        detailTvShowViewModel.getDetailTvShowsVideo().observe(this) {
            if (it != null) {
                binding.apply {

                    when {
                        it.results.isEmpty() -> {
                            tvTrailer.text =
                                String.format(Locale.getDefault(), "Trailer Video Not Available")
                        }

                        else -> {
                            tvTrailer.text = it.results[0].name
                        }
                    }

                    lifecycle.addObserver(videoTrailer)
                    videoTrailer.addYouTubePlayerListener(object : AbstractYouTubePlayerListener() {
                        override fun onReady(youTubePlayer: YouTubePlayer) {

                            when {
                                it.results.isEmpty() -> {
                                    Log.e("videoFailed", "onReady: ")
                                }

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

    private fun setCastAdapter() {
        castAdapter = TvShowsCastAdapter()
    }

    private fun setRecyclerView() {
        with(binding.rvTvShowCast) {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            setHasFixedSize(true)
            adapter = castAdapter
        }
    }

    private fun setCast() {
//        detailTvShowViewModel.setDetailTvShowsCast(id, BuildConfig.API_KEY)
        detailTvShowViewModel.getDetailTvShowsCast().observe(this) {
            if (it != null) {
//                castAdapter.setTvShowsCasts(it)
            }
        }
    }

    private fun setReadMore() {
        binding.apply {
            tvReadMore.visibility = View.VISIBLE
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
    }

    private fun ImageView.loadImage(url: String) {
        Glide.with(this.context)
                .load(UrlEndpoint.IMAGE_URL + url)
                .apply(RequestOptions.placeholderOf(R.drawable.ic_loading)
                                .error(R.drawable.ic_error)
                )
                .into(this)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.detail_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {

            android.R.id.home -> onBackPressed()

            R.id.nav_share -> {
                try {

                    val body = "Visit this awesome shows \n${data.homepage}"

                    val shareIntent = Intent(Intent.ACTION_SEND)
                    shareIntent.type = "text/plain"
                    shareIntent.putExtra(Intent.EXTRA_TEXT, body)
                    startActivity(Intent.createChooser(shareIntent, "Share with:"))
                } catch (e: Exception) {
                    Log.e("shareFailed", "onOptionsItemSelected: ${e.localizedMessage}")
                }
            }

            R.id.nav_open_in_browser -> {
                try {
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(data.homepage))
                    startActivity(Intent.createChooser(intent, "Open with:"))
                } catch (e: Exception) {
                    Toasty.warning(
                        this,
                        "Silakan install browser terlebih dulu.",
                        Toast.LENGTH_SHORT, true
                    ).show()

                    Log.e("errorLink", "setdetailTvShowViewModel: ${e.localizedMessage}" )
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }

    companion object {
        const val EXTRA_DETAIL_TV = "com.taufik.themovieshow.ui.tvshow.activity.EXTRA_DETAIL_TV"
    }
}