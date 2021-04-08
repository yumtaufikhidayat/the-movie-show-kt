package com.taufik.themovieshow.ui.main.movie.ui.activity

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
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.taufik.themovieshow.BuildConfig
import com.taufik.themovieshow.R
import com.taufik.themovieshow.api.UrlEndpoint
import com.taufik.themovieshow.databinding.ActivityDetailMovieBinding
import com.taufik.themovieshow.ui.main.movie.data.detail.MovieDetailResponse
import com.taufik.themovieshow.ui.main.movie.viewmodel.DetailMovieViewModel
import com.taufik.themovieshow.utils.DataDummy
import es.dmoral.toasty.Toasty
import kotlin.properties.Delegates

class DetailMovieActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_DETAIL_ID = "com.taufik.themovieshow.ui.main.movie.ui.activity.EXTRA_DETAIL_ID"
        const val EXTRA_DETAIL_TITLE = "com.taufik.themovieshow.ui.main.movie.ui.activity.EXTRA_DETAIL_TITLE"
    }

    private lateinit var binding: ActivityDetailMovieBinding
    private lateinit var viewModel: DetailMovieViewModel
    private var id by Delegates.notNull<Int>()
    private lateinit var title: String
    private lateinit var data: MovieDetailResponse

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailMovieBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setParcelableData()

        initActionBar()

        setData()

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
//        viewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory())[DetailMovieViewModel::class.java]
//        viewModel.setDetailMovieNowPlaying(id, BuildConfig.API_KEY)
//        viewModel.getDetailMovieNowPlaying().observe(this, {
//            data = it
//            if (it != null) {
//                binding.apply {
//                    imgPoster.loadImage(it.posterPath)
//                    imgBackdrop.loadImage(it.backdropPath)
//                    tvTitle.text = it.title
//                    tvReleaseDate.text = it.releaseDate
//                    tvStatus.text = it.status
//                    tvOverview.text = it.overview
//                    tvRating.text = it.voteAverage.toString()
//
//                    if (it.genres.isEmpty()) {
//                        tvGenre.text = "N/A"
//                    } else {
//                        tvGenre.text = it.genres[0].name
//                    }
//
//                    tvRuntime.text = it.runtime.toString()
//                    tvLanguage.text = it.originalLanguage
//
//                    val websiteLink = it.homepage
//
//                    btnWebsite.setOnClickListener {
//                        try {
//                            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(websiteLink))
//                            startActivity(Intent.createChooser(intent, "Open with:"))
//                        } catch (e: Exception) {
//                            Toasty.warning(
//                                    this@DetailMovieActivity,
//                                    "Silakan install browser terlebih dulu.",
//                                    Toast.LENGTH_SHORT, true
//                            ).show()
//
//                            Log.e("errorLink", "setViewModel: ${e.localizedMessage}" )
//                        }
//                    }
//                }
//            }
//        })
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

            R.id.nav_open_in_browser -> {
                try {
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(data.homepage))
                    startActivity(Intent.createChooser(intent, "Open with:"))
                } catch (e: Exception) {
                    Toasty.warning(
                        this@DetailMovieActivity,
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