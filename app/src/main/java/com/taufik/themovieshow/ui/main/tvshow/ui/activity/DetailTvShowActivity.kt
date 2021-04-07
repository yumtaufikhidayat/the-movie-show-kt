package com.taufik.themovieshow.ui.main.tvshow.ui.activity

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.taufik.themovieshow.BuildConfig
import com.taufik.themovieshow.R
import com.taufik.themovieshow.api.UrlEndpoint
import com.taufik.themovieshow.databinding.ActivityDetailTvShowBinding
import com.taufik.themovieshow.ui.main.tvshow.viewmodel.DetailTvShowViewModel
import es.dmoral.toasty.Toasty
import kotlin.properties.Delegates

class DetailTvShowActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_DETAIL_ID = "com.taufik.themovieshow.ui.main.movie.ui.activity.EXTRA_DETAIL_ID"
        const val EXTRA_DETAIL_TITLE = "com.taufik.themovieshow.ui.main.movie.ui.activity.EXTRA_DETAIL_TITLE"
    }

    private lateinit var binding: ActivityDetailTvShowBinding
    private lateinit var viewModel: DetailTvShowViewModel
    private var id by Delegates.notNull<Int>()
    private lateinit var title: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailTvShowBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setParcelableData()

        initActionBar()

        setData()
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

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            android.R.id.home -> onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setData() {
        viewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory())[DetailTvShowViewModel::class.java]
        viewModel.setDetailTvShowPopular(id, BuildConfig.API_KEY)
        viewModel.getDetailTvShowsPopular().observe(this, {
            if (it != null) {
                binding.apply {
                    imgPoster.loadImage(it.posterPath)
                    imgBackdrop.loadImage(it.backdropPath)
                    tvTitle.text = it.name

                    if (it.networks[0].originCountry.isEmpty()) {
                        tvNetwork.text = String.format("${it.networks[0].name} (N/A)")
                    } else{
                        tvNetwork.text = String.format("${it.networks[0].name} (${it.networks[0].originCountry})")
                    }

                    tvReleaseDate.text = it.firstAirDate
                    tvStatus.text = it.status
                    tvOverview.text = it.overview
                    tvRating.text = it.voteAverage.toString()
                    tvGenre.text = it.genres[0].name
                    tvEpisodes.text = it.episodeRunTime[0].toString()
                    tvLanguage.text = it.spokenLanguages[0].name

                    val websiteLink = it.homepage

                    btnWebsite.setOnClickListener {
                        try {
                            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(websiteLink))
                            startActivity(Intent.createChooser(intent, "Open with:"))
                        } catch (e: Exception) {
                            Toasty.warning(
                                this@DetailTvShowActivity,
                                "Silakan install browser terlebih dulu.",
                                Toast.LENGTH_SHORT, true
                            ).show()

                            Log.e("errorLink", "setViewModel: ${e.localizedMessage}" )
                        }
                    }
                }
            }
        })
    }

    private fun ImageView.loadImage(url: String) {
        Glide.with(this.context)
                .load(UrlEndpoint.IMAGE_URL + url)
                .apply(RequestOptions.placeholderOf(R.drawable.ic_loading)
                                .error(R.drawable.ic_error)
                )
                .into(this)
    }
}