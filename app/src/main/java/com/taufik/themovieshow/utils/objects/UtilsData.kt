package com.taufik.themovieshow.utils.objects

import android.content.Context
import com.taufik.themovieshow.BuildConfig
import com.taufik.themovieshow.R
import com.taufik.themovieshow.model.about.AboutSection
import com.taufik.themovieshow.model.favorite.SortFiltering
import com.taufik.themovieshow.model.response.about.About
import com.taufik.themovieshow.model.response.about.AboutAction
import com.taufik.themovieshow.utils.language.LANGUAGE
import com.taufik.themovieshow.utils.language.LanguageCache

object UtilsData {
    fun getGeneratedAboutData(context: Context): List<AboutSection> = mutableListOf(
        AboutSection.AuthorSection(
            context.getString(R.string.tvDeveloper),
            generateAboutAuthorData(context)
        ),
        AboutSection.ApplicationSection(
            context.getString(R.string.tvApplication),
            generateAboutApplicationData(context)
        )
    )

    private fun generateAboutAuthorData(context: Context): List<About> = mutableListOf(
        About(
            AboutAction.LinkedIn,
            R.drawable.ic_outline_profile,
            context.getString(R.string.tvTitleLetsGreet),
            context.getString(R.string.tvDescLetsGreet)
        ),
        About(
            AboutAction.GitHub,
            R.drawable.github,
            context.getString(R.string.tvTitleForkOnGithub),
            context.getString(R.string.tvDescForkOnGithub),
        ),
        About(
            AboutAction.Email,
            R.drawable.ic_outline_email,
            context.getString(R.string.tvTitleSendEmail),
            context.getString(R.string.tvDescSendEmail),
        )
    )

    private fun generateAboutApplicationData(context: Context): List<About> {
        val currentLanguageCode = LanguageCache.get(context)
        val languageText: String = when (currentLanguageCode) {
            LANGUAGE.INDONESIA.code -> context.getString(R.string.tvLanguageIndonesia)
            LANGUAGE.ENGLISH.code -> context.getString(R.string.tvLanguageEnglish)
            else -> context.getString(R.string.tvLanguageEnglish)
        }

        val aboutList = mutableListOf(
            About(
                AboutAction.LanguageSetting,
                R.drawable.ic_language,
                context.getString(R.string.tvLanguage),
                languageText
            ),
            About(
                AboutAction.Email,
                R.drawable.ic_outline_bug,
                context.getString(R.string.tvTitleReportIssue),
                context.getString(R.string.tvDescReportIssue)
            ),
            About(
                AboutAction.GooglePlay,
                R.drawable.ic_outline_rate,
                context.getString(R.string.tvTitleRateThisApp),
                context.getString(R.string.tvDescRateThisApp)
            ),
            About(
                AboutAction.NoOp,
                R.drawable.ic_outline_copyright,
                context.getString(R.string.tvTitleCopyright),
                context.getString(R.string.tvDescCopyright)
            ),
            About(
                AboutAction.NoOp,
                R.drawable.ic_update,
                context.getString(R.string.tvTitleVersion),
                BuildConfig.VERSION_NAME
            ),
        )

        return aboutList
    }

    fun generateSortFilteringData(): List<SortFiltering> {
        return mutableListOf(
            SortFiltering(sortId = 0, sortNameRes = R.string.tvSortAll),
            SortFiltering(sortId = 1, sortNameRes = R.string.tvSortTitle),
            SortFiltering(sortId = 2, sortNameRes = R.string.tvSortRelease),
            SortFiltering(sortId = 3, sortNameRes = R.string.tvRating)
        )
    }
}