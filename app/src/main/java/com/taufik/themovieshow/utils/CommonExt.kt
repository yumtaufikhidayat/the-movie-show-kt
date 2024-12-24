package com.taufik.themovieshow.utils

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.taufik.themovieshow.BuildConfig
import com.taufik.themovieshow.R
import com.taufik.themovieshow.data.remote.api.UrlEndpoint
import com.taufik.themovieshow.databinding.LayoutErrorBinding
import com.taufik.themovieshow.ui.detail.movie.fragment.DetailMovieFragment
import com.taufik.themovieshow.ui.detail.tvshow.fragment.DetailTvShowFragment
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

inline fun <T> List<T>.filterAndSortByDate(
    crossinline getDate: (T) -> String?,
    dateThreshold: String = "01-01-2010",
    inputFormat: String,
    thresholdFormat: String
): List<T> {
    val dateThresholdParsed = SimpleDateFormat(thresholdFormat, Locale.US).parse(dateThreshold)

    return this.filter { item ->
        try {
            val dateString = getDate(item)
            if (dateString != null) {
                val itemDate = SimpleDateFormat(inputFormat, Locale.US).parse(dateString)
                itemDate != null && (itemDate.after(dateThresholdParsed) || itemDate == dateThresholdParsed)
            } else {
                false
            }
        } catch (e: Exception) {
            false
        }
    }.sortedByDescending { item ->
        try {
            val dateString = getDate(item)
            if (dateString != null) {
                SimpleDateFormat(inputFormat, Locale.US).parse(dateString)
            } else {
                Date(0) // Default to the earliest possible date if parsing fails
            }
        } catch (e: Exception) {
            Date(0) // Default to the earliest possible date if parsing fails
        }
    }
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
    val bundle = bundleOf(
        DetailMovieFragment.EXTRA_ID to id,
        DetailMovieFragment.EXTRA_TITLE to title
    )
    this.findNavController().navigate(R.id.detailMovieFragment, bundle)
}

fun Fragment.navigateToDetailTvShow(id: Int, title: String) {
    val bundle = bundleOf(
        DetailTvShowFragment.EXTRA_ID to id,
        DetailTvShowFragment.EXTRA_TITLE to title
    )
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

fun TextView.stringFormat(data1: String, data2: String) {
    this.text = String.format(
        "%s %s",
        data1,
        data2
    )
}

fun View.showLoading() {
    this.isVisible = true
}

fun View.hideLoading() {
    this.isVisible = false
}

fun LayoutErrorBinding.showError(message: String?) {
    root.isVisible = true
    tvErrorDesc.text = message
}