package com.taufik.themovieshow.model.about

import com.taufik.themovieshow.model.response.about.About

sealed class AboutSection {
    data class AuthorSection(val title: String, val items: List<About>) : AboutSection()
    data class ApplicationSection(val title: String, val items: List<About>) : AboutSection()
}