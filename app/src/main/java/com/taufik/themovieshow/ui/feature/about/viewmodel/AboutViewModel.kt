package com.taufik.themovieshow.ui.feature.about.viewmodel

import androidx.lifecycle.ViewModel
import com.taufik.themovieshow.ui.feature.about.data.About
import com.taufik.themovieshow.utils.UtilsData

class AboutViewModel : ViewModel() {

    fun getAboutAuthor(): List<About> = UtilsData.generateAboutAuthorData()

    fun getAboutApplication(): List<About> = UtilsData.generateAboutApplicationData()
}