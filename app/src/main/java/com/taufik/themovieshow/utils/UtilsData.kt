package com.taufik.themovieshow.utils

import android.content.Context
import com.taufik.themovieshow.BuildConfig
import com.taufik.themovieshow.R
import com.taufik.themovieshow.model.response.about.About

object UtilsData {
    fun generateAboutAuthorData(context: Context): List<About> {
        return mutableListOf(
            About(
                R.drawable.ic_outline_profile,
                context.getString(R.string.tvTitleLetsGreet),
                context.getString(R.string.tvDescLetsGreet)
            ),
            About(
                R.drawable.github,
                context.getString(R.string.tvTitleForkOnGithub),
                context.getString(R.string.tvDescForkOnGithub),
            ),
            About(
                R.drawable.ic_outline_email,
                context.getString(R.string.tvTitleSendEmail),
                context.getString(R.string.tvDescSendEmail),
            )
        )
    }

    fun generateAboutApplicationData(context: Context): List<About> {
        return mutableListOf(
            About(
                R.drawable.ic_outline_copyright,
                context.getString(R.string.tvTitleCopyright),
                context.getString(R.string.tvDescCopyright)
            ),
            About(
                R.drawable.ic_update,
                context.getString(R.string.tvTitleVersion),
                BuildConfig.VERSION_NAME
            ),
            About(
                R.drawable.ic_outline_rate,
                context.getString(R.string.tvTitleRateThisApp),
                context.getString(R.string.tvDescRateThisApp)
            ),
            About(
                R.drawable.ic_outline_bug,
                context.getString(R.string.tvTitleReportIssue),
                context.getString(R.string.tvDescReportIssue)
            )
        )
    }
}