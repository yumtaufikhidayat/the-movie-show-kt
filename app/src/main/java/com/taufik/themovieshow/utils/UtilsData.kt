package com.taufik.themovieshow.utils

import com.taufik.themovieshow.BuildConfig
import com.taufik.themovieshow.R
import com.taufik.themovieshow.model.response.about.About

object UtilsData {
    fun generateAboutAuthorData(): List<About> {

        return mutableListOf(
            About(
                R.drawable.ic_outline_profile,
                "Let's greet",
                "Taufik Hidayat"
            ),
            About(
                R.drawable.github,
                "Fork on Github",
                "Fork this app on Github"
            ),
            About(
                R.drawable.ic_outline_email,
                "Send an email",
                "yumtaufikhidayat@gmail.com"
            )
        )
    }

    fun generateAboutApplicationData(): List<About> {
        return mutableListOf(
            About(
                R.drawable.ic_update,
                "Version",
                BuildConfig.VERSION_NAME
            ),
            About(
                R.drawable.ic_outline_rate,
                "Rate this app",
                "Like this app? Rate 5 stars"
            ),
            About(
                R.drawable.ic_outline_bug,
                "Report an issue",
                "Having an issue? Report in here"
            )
        )
    }
}