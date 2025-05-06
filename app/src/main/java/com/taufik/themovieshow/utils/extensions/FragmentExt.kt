package com.taufik.themovieshow.utils.extensions

import android.view.View
import androidx.core.os.bundleOf
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.tabs.TabLayout
import com.taufik.themovieshow.R
import com.taufik.themovieshow.ui.detail.movie.fragment.DetailMovieFragment
import com.taufik.themovieshow.ui.detail.tvshow.fragment.DetailTvShowFragment
import android.graphics.Typeface
import android.widget.TextView
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayoutMediator
import com.taufik.themovieshow.ui.common.adapter.TabPagerAdapter

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

fun Fragment.setupTabLayoutBinding(
    tabLayout: TabLayout,
    viewPager: ViewPager2,
    fragments: List<Fragment>,
    @StringRes tabTitles: IntArray,
    defaultTabIndex: Int = 1
) {
    viewPager.adapter = TabPagerAdapter(fragments, this)

    TabLayoutMediator(tabLayout, viewPager) { tab, position ->
        tab.customView = requireContext().createCustomTabView(
            titleRes = tabTitles[position],
            isSelected = position == defaultTabIndex
        )
    }.attach()

    viewPager.setCurrentItem(defaultTabIndex, false)
    viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
        override fun onPageSelected(position: Int) {
            super.onPageSelected(position)
            for (i in 0 until tabLayout.tabCount) {
                val tab = tabLayout.getTabAt(i)
                val textView = tab?.customView?.findViewById<TextView>(R.id.tabText) ?: continue

                val isSelected = i == position
                textView.animate()
                    .scaleX(if (isSelected) 1f else 0.95f)
                    .scaleY(if (isSelected) 1f else 0.95f)
                    .setDuration(150)
                    .start()

                textView.setTypeface(null, if (isSelected) Typeface.BOLD else Typeface.NORMAL)
                textView.setTextColor(
                    ContextCompat.getColor(
                        textView.context,
                        if (isSelected) android.R.color.white else R.color.colorSemiBlack
                    )
                )

                textView.background = ContextCompat.getDrawable(
                    textView.context,
                    if (isSelected) R.drawable.bg_tab else android.R.color.transparent
                )
            }
        }
    })

//    adjustTabLayoutMode(tabLayout)
}

fun Fragment.adjustTabLayoutMode(tabLayout: TabLayout) {
    tabLayout.post {
        val totalTabWidth = (0 until tabLayout.tabCount).sumOf { index ->
            val tab = tabLayout.getTabAt(index)
            val textView = tab?.customView?.findViewById<TextView>(R.id.tabText)
            textView?.paint?.measureText(textView.text.toString())?.toInt() ?: 0
        }

        val screenWidth = resources.displayMetrics.widthPixels

        tabLayout.tabMode = if (totalTabWidth > screenWidth) {
            TabLayout.MODE_SCROLLABLE
        } else {
            TabLayout.MODE_FIXED
        }
    }
}