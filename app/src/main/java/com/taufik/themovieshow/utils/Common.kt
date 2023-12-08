package com.taufik.themovieshow.utils

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.taufik.themovieshow.BuildConfig
import com.taufik.themovieshow.R
import com.taufik.themovieshow.data.remote.api.UrlEndpoint
import com.taufik.themovieshow.ui.detail.movie.fragment.DetailMovieFragment
import com.taufik.themovieshow.ui.detail.tvshow.fragment.DetailTvShowFragment
import com.taufik.themovieshow.ui.favorite.fragment.FavoriteTvShowsFragment
import es.dmoral.toasty.Toasty
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.math.roundToInt

fun ImageView.loadImage(url: String?) {
    Glide.with(this.context)
        .load(BuildConfig.IMAGE_URL + url)
        .apply(RequestOptions.placeholderOf(R.drawable.ic_loading).error(R.drawable.ic_error))
        .into(this)
}

fun ImageView.loadVideoThumbnail(url: String?) {
    Glide.with(this.context)
        .load(BuildConfig.THUMBNAIL_IMAGE_URL + url + UrlEndpoint.THUMBNAIL_IMAGE_JPG)
        .apply(RequestOptions.placeholderOf(R.drawable.ic_loading).error(R.drawable.ic_error))
        .into(this)
}

fun String.convertDate(inputFormat: String, outputFormat: String): String {
    val formatter = SimpleDateFormat(inputFormat, Locale.US)
    var formatParser = Date()
    if (this.isNotEmpty()) {
        formatParser = formatter.parse(this) ?: Date()
    }
    val newOutputFormat = SimpleDateFormat(outputFormat, Locale.US)
    return newOutputFormat.format(formatParser)
}

fun toRating(data: Double): String {
    val tenDouble = 10.0
    return ((data * tenDouble).roundToInt() / tenDouble).toString()
}

fun Context.showSuccessToastyIcon(message: String) {
    Toasty.success(this, message, Toast.LENGTH_SHORT, true).show()
}

fun Context.showSuccessToasty(message: String) {
    Toasty.success(this, message, Toast.LENGTH_SHORT, false).show()
}

fun Fragment.navigateToDetailMovie(id: Int, title: String) {
    val bundle = Bundle().apply {
        putInt(DetailMovieFragment.EXTRA_ID, id)
        putString(DetailMovieFragment.EXTRA_TITLE, title)
    }
    this.findNavController().navigate(R.id.detailMovieFragment, bundle)
}

fun Fragment.navigateToDetailTvShow(id: Int, title: String, position: Int) {
    val bundle = Bundle().apply {
        putInt(DetailTvShowFragment.EXTRA_ID, id)
        putString(DetailTvShowFragment.EXTRA_TITLE, title)
        putInt(FavoriteTvShowsFragment.POSITION_KEY, position)
    }
    this.findNavController().navigate(R.id.detailTvShowFragment, bundle)
}

fun Context.share(shareText: String, link: String) {
    try {
        val body = StringBuilder(shareText).append(link).toString()
        val shareIntent = Intent(Intent.ACTION_SEND)
        shareIntent.apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT, body)
        }
        startActivity(
            Intent.createChooser(
                shareIntent,
                getString(R.string.tvShareWith)
            )
        )
    } catch (e: Exception) {
        showSuccessToastyIcon(getString(R.string.tvOops))
    }
}

fun Fragment.popBackStack() = findNavController().popBackStack()

fun Context.showTrailerVideo(key: String) {
    try {
        startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube://$key")))
    } catch (e: Exception) {
        this.showSuccessToastyIcon(getString(R.string.tvInstallBrowserYouTube, e.localizedMessage))
    }
}