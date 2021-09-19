package com.taufik.themovieshow.ui.about.viewmodel

import androidx.lifecycle.ViewModel
import com.taufik.themovieshow.ui.about.model.About
import com.taufik.themovieshow.utils.UtilsData

class AboutViewModel : ViewModel() {

    fun getAboutAuthor(): List<About> = UtilsData.generateAboutAuthorData()

    fun getAboutApplication(): List<About> = UtilsData.generateAboutApplicationData()
}