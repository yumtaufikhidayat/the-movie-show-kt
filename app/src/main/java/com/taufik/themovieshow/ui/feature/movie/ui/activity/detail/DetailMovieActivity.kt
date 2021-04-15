package com.taufik.themovieshow.ui.feature.movie.ui.activity.detail

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
import com.taufik.themovieshow.databinding.ActivityDetailMovieBinding
import com.taufik.themovieshow.ui.feature.movie.data.detail.MovieDetailResponse
import com.taufik.themovieshow.ui.feature.movie.ui.adapter.MovieCastAdapter
import com.taufik.themovieshow.ui.feature.movie.viewmodel.DetailMovieViewModel
import es.dmoral.toasty.Toasty
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*
import kotlin.properties.Delegates

class DetailMovieActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_DETAIL_ID = "com.taufik.themovieshow.ui.feature.movie.ui.activity.EXTRA_DETAIL_ID"
        const val EXTRA_DETAIL_TITLE = "com.taufik.themovieshow.ui.feature.movie.ui.activity.EXTRA_DETAIL_TITLE"
        const val EXTRA_POSTER = "com.taufik.themovieshow.ui.feature.movie.ui.activity.detail.EXTRA_POSTER"
        const val EXTRA_RELEASE_DATE = "com.taufik.themovieshow.ui.feature.movie.ui.activity.detail.EXTRA_RELEASE_DATE"
        const val EXTRA_RATING = "com.taufik.themovieshow.ui.feature.movie.ui.activity.detail.EXTRA_RATING"
    }

    private lateinit var binding: ActivityDetailMovieBinding
    private lateinit var viewModel: DetailMovieViewModel
    private lateinit var castAdapter: MovieCastAdapter
    private var id by Delegates.notNull<Int>()
    private lateinit var title: String
    private lateinit var data: MovieDetailResponse
    private lateinit var moviePoster: String
    private lateinit var movieReleaseDate: String
    private var movieRating by Delegates.notNull<Double>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailMovieBinding.inflate(layoutInflater)
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
        moviePoster = intent.getStringExtra(EXTRA_POSTER).toString()
        movieReleaseDate = intent.getStringExtra(EXTRA_RELEASE_DATE).toString()
        movieRating = intent.getDoubleExtra(EXTRA_RATING, 0.0)
    }

    private fun initActionBar() {
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setIcon(R.drawable.ic_arrow_back)
        supportActionBar?.title = title
        supportActionBar?.elevation = 0F
    }

    private fun setData() {
        viewModel = ViewModelProvider(this)[DetailMovieViewModel::class.java]
        viewModel.setDetailMovies(id, BuildConfig.API_KEY)
        viewModel.getDetailMovies().observe(this, {
            data = it
            if (it != null) {
                binding.apply {
                    imgPoster.loadImage(it.posterPath)
                    imgBackdrop.loadImage(it.backdropPath)
                    tvTitle.text = it.title
                    tvReleaseDate.text = it.releaseDate
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

                    tvRuntime.text = String.format("${it.runtime} min")
                    tvLanguage.text = it.originalLanguage

                    val webLink = it.homepage
                    btnWebsite.setOnClickListener {
                        try {
                            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(webLink))
                            startActivity(Intent.createChooser(intent, "Open with:"))
                        } catch (e: Exception) {
                            Toasty.warning(
                                this@DetailMovieActivity,
                                "Please install browser.",
                                Toast.LENGTH_SHORT, true
                            ).show()

                            Log.e("errorLink", "setViewModel: ${e.localizedMessage}" )
                        }
                    }
                }
            }
        })

        var isChecked = false
        binding.apply {
            CoroutineScope(Dispatchers.IO).launch {
                val count = viewModel.checkFavorite(id)
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

        binding.toggleFavorite.setOnClickListener{
            isChecked = !isChecked
            if (isChecked) {
                viewModel.addToFavorite(id, data.posterPath, title, data.releaseDate, data.voteAverage)
                Toasty.success(this, "Ditambahkan ke favorit", Toast.LENGTH_SHORT, true).show()
            } else {
                viewModel.removeFromFavorite(id)
                Toasty.success(this, "Dihapus dari favorit", Toast.LENGTH_SHORT, true).show()
            }

            binding.toggleFavorite.isChecked = isChecked
        }
    }

    private fun setVideo() {
        viewModel.setDetailMovieVideo(id, BuildConfig.API_KEY)
        viewModel.getDetailMovieVideo().observe(this, {
            if (it != null) {
                binding.apply {

                    when {
                        it.results.isEmpty() -> {
                            tvTrailer.text = String.format(Locale.getDefault(), "Trailer Video Not Available")
                        }

                        else -> {
                            tvTrailer.text = it.results[0].name
                        }
                    }

                    lifecycle.addObserver(videoTrailer)
                    videoTrailer.addYouTubePlayerListener(object : AbstractYouTubePlayerListener(){
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
        castAdapter = MovieCastAdapter()
        castAdapter.notifyDataSetChanged()
    }

    private fun setRecyclerView() {
        with(binding.rvMovieCast) {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            setHasFixedSize(true)
            adapter = castAdapter
        }
    }

    private fun setCast() {
        viewModel.setDetailMovieCast(id, BuildConfig.API_KEY)
        viewModel.getDetailMovieCast().observe(this, {
            if (it != null) {
                castAdapter.setMovieCasts(it)
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

                    val body = "Visit this awesome movie \n${data.homepage}"

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
                        this@DetailMovieActivity,
                        "Please install browser.",
                        Toast.LENGTH_SHORT, true
                    ).show()

                    Log.e("errorLink", "setViewModel: ${e.localizedMessage}" )
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }
}