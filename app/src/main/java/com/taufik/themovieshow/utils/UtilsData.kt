package com.taufik.themovieshow.utils

import android.content.Context
import com.taufik.themovieshow.BuildConfig
import com.taufik.themovieshow.R
import com.taufik.themovieshow.model.about.AboutSection
import com.taufik.themovieshow.model.favorite.SortFiltering
import com.taufik.themovieshow.model.response.about.About
import com.taufik.themovieshow.model.response.about.AboutAction

object UtilsData {
    fun Context.getAboutData(): List<AboutSection> = mutableListOf(
        AboutSection.AuthorSection(
            this.getString(R.string.tvDeveloper),
            this.generateAboutAuthorData()
        ),
        AboutSection.ApplicationSection(
            this.getString(R.string.tvApplication),
            this.generateAboutApplicationData()
        )
    )

    private fun Context.generateAboutAuthorData(): List<About> = mutableListOf(
        About(
            AboutAction.LinkedIn,
            R.drawable.ic_outline_profile,
            this.getString(R.string.tvTitleLetsGreet),
            this.getString(R.string.tvDescLetsGreet)
        ),
        About(
            AboutAction.GitHub,
            R.drawable.github,
            this.getString(R.string.tvTitleForkOnGithub),
            this.getString(R.string.tvDescForkOnGithub),
        ),
        About(
            AboutAction.Email,
            R.drawable.ic_outline_email,
            this.getString(R.string.tvTitleSendEmail),
            this.getString(R.string.tvDescSendEmail),
        )
    )

    private fun Context.generateAboutApplicationData(): List<About> = mutableListOf(
        About(
            AboutAction.LanguageSetting,
            R.drawable.ic_language,
            this.getString(R.string.tvLanguage),
            this.getString(R.string.tvDescLanguage)
        ),
        About(
            AboutAction.Email,
            R.drawable.ic_outline_bug,
            this.getString(R.string.tvTitleReportIssue),
            this.getString(R.string.tvDescReportIssue)
        ),
        About(
            AboutAction.GooglePlay,
            R.drawable.ic_outline_rate,
            this.getString(R.string.tvTitleRateThisApp),
            this.getString(R.string.tvDescRateThisApp)
        ),
        About(
            AboutAction.NoOp,
            R.drawable.ic_outline_copyright,
            this.getString(R.string.tvTitleCopyright),
            this.getString(R.string.tvDescCopyright)
        ),
        About(
            AboutAction.NoOp,
            R.drawable.ic_update,
            this.getString(R.string.tvTitleVersion),
            BuildConfig.VERSION_NAME
        ),
    )

    fun generateSortFilteringData(): List<SortFiltering> {
        return mutableListOf(
            SortFiltering(sortId = 0, sortNameRes = R.string.tvSortAll),
            SortFiltering(sortId = 1, sortNameRes = R.string.tvSortTitle),
            SortFiltering(sortId = 2, sortNameRes = R.string.tvSortRelease),
            SortFiltering(sortId = 3, sortNameRes = R.string.tvRating)
        )
    }
}