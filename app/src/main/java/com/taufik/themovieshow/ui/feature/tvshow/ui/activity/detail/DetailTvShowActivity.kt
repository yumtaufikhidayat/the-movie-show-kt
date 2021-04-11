package com.taufik.themovieshow.ui.feature.tvshow.ui.activity.detail

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
import com.taufik.themovieshow.BuildConfig
import com.taufik.themovieshow.R
import com.taufik.themovieshow.api.UrlEndpoint
import com.taufik.themovieshow.databinding.ActivityDetailTvShowBinding
import com.taufik.themovieshow.ui.feature.tvshow.data.detail.TvShowsPopularDetailResponse
import com.taufik.themovieshow.ui.feature.tvshow.ui.adapter.TvShowsCastAdapter
import com.taufik.themovieshow.ui.feature.tvshow.viewmodel.DetailTvShowViewModel
import es.dmoral.toasty.Toasty
import java.util.*
import kotlin.properties.Delegates

class DetailTvShowActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_DETAIL_ID = "com.taufik.themovieshow.ui.feature.movie.ui.activity.EXTRA_DETAIL_ID"
        const val EXTRA_DETAIL_TITLE = "com.taufik.themovieshow.ui.feature.movie.ui.activity.EXTRA_DETAIL_TITLE"
    }

    private lateinit var binding: ActivityDetailTvShowBinding
    private lateinit var viewModel: DetailTvShowViewModel
    private var id by Delegates.notNull<Int>()
    private lateinit var title: String
    private lateinit var data: TvShowsPopularDetailResponse
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
        id = intent.getIntExtra(EXTRA_DETAIL_ID, 0)
        title = intent.getStringExtra(EXTRA_DETAIL_TITLE).toString()
    }

    private fun initActionBar() {
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setIcon(R.drawable.ic_arrow_back)
        supportActionBar?.title = title
        supportActionBar?.elevation = 0F
    }

    private fun setData() {
        viewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory())[DetailTvShowViewModel::class.java]
        viewModel.setDetailTvShowPopular(id, BuildConfig.API_KEY)
        viewModel.getDetailTvShowsPopular().observe(this, {
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
                            tvNetwork.text = String.format("${it.networks[0].name} (${it.networks[0].originCountry})")
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

                    val webLink = it.homepage
                    btnWebsite.setOnClickListener {
                        try {
                            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(webLink))
                            startActivity(Intent.createChooser(intent, "Open with:"))
                        } catch (e: Exception) {
                            Toasty.warning(
                                    this@DetailTvShowActivity,
                                    "Please install browser.",
                                    Toast.LENGTH_SHORT, true
                            ).show()

                            Log.e("errorLink", "setViewModel: ${e.localizedMessage}")
                        }
                    }
                }
            }
        })
    }

    private fun setVideo() {
        viewModel.setDetailTvShowVideo(id, BuildConfig.API_KEY)
        viewModel.getDetailTvShowsVideo().observe(this, {
            if (it != null) {
                binding.apply {

                    if (it.results.isEmpty()) {
                        tvTrailer.text = String.format(Locale.getDefault(), "Trailer Video")
                    } else {
                        tvTrailer.text = it.results[0].name
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
        })
    }

    private fun setCastAdapter() {
        castAdapter = TvShowsCastAdapter()
        castAdapter.notifyDataSetChanged()
    }

    private fun setRecyclerView() {
        with(binding.rvTvShowCast) {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            setHasFixedSize(true)
            adapter = castAdapter
        }
    }

    private fun setCast() {
        viewModel.setDetailTvShowsCast(id, BuildConfig.API_KEY)
        viewModel.getDetailTvShowsCast().observe(this, {
            if (it != null) {
                castAdapter.setTvShowsCasts(it)
            }
        })
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

                    Log.e("errorLink", "setViewModel: ${e.localizedMessage}" )
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }
}