package com.taufik.themovieshow.utils.extensions

import android.view.View
import androidx.core.os.bundleOf
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.taufik.themovieshow.R
import com.taufik.themovieshow.ui.detail.movie.fragment.DetailMovieFragment
import com.taufik.themovieshow.ui.detail.tvshow.fragment.DetailTvShowFragment

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

fun Fragment.popBackStack() = findNavController().popBackStack()

fun Fragment.applySystemBarBottomPadding(targetView: View? = null) {
    val viewToApply = targetView ?: view ?: return

    ViewCompat.setOnApplyWindowInsetsListener(viewToApply) { view, insets ->
        val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
        view.setPadding(
            view.paddingLeft,
            view.paddingTop,
            view.paddingRight,
            systemBars.bottom
        )
        insets
    }

    // Request insets (penting agar diproses saat view sudah attached)
    viewToApply.requestApplyInsets()
}
