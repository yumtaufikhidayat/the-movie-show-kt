package com.taufik.themovieshow.ui.detail.activity

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.taufik.themovieshow.R
import com.taufik.themovieshow.data.MovieShowDetail
import com.taufik.themovieshow.databinding.ActivityDetailBinding
import com.taufik.themovieshow.ui.detail.viewmodel.DetailViewModel
import kotlin.properties.Delegates

class DetailActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_DETAIL_ID = "com.taufik.themovieshow.ui.detail.activity.EXTRA_DETAIL_ID"
        const val EXTRA_DETAIL_TITLE = "com.taufik.themovieshow.ui.detail.activity.EXTRA_DETAIL_TITLE"
    }

    private lateinit var binding: ActivityDetailBinding
    private var id by Delegates.notNull<Int>()
    private lateinit var title: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
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
        val viewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory())[DetailViewModel::class.java]
        viewModel.setSelectedDetail(id)
//        populateDetailData(viewModel.getSelectedDetail())
    }

    private fun populateDetailData(movieShowDetail: MovieShowDetail) {
        binding.apply {
            Glide.with(this@DetailActivity)
                .load(movieShowDetail.imageBackdrop)
                .transform(RoundedCorners(20))
                .placeholder(R.color.colorPrimaryDark)
                .into(imgBackdrop)

            Glide.with(this@DetailActivity)
                .load(movieShowDetail.imagePoster)
                .transform(RoundedCorners(20))
                .apply(RequestOptions.placeholderOf(R.drawable.ic_loading)
                    .error(R.drawable.ic_error))
                .into(imgPoster)

            tvTitle.text = movieShowDetail.title
            tvCompanyProduction.text = movieShowDetail.productionCompanies
            tvReleaseDate.text = movieShowDetail.releaseDate
            tvStatus.text = movieShowDetail.status
            tvOverview.text = movieShowDetail.overview
            tvRating.text = movieShowDetail.rate.toString()
            tvGenre.text = movieShowDetail.genre
            tvRuntime.text = movieShowDetail.runtime
        }
    }
}