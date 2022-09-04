package com.taufik.themovieshow.data.viewmodel.about

import androidx.lifecycle.ViewModel
import com.taufik.themovieshow.data.main.about.About
import com.taufik.themovieshow.utils.UtilsData

class AboutViewModel : ViewModel() {
    fun getAboutAuthor(): List<About> = UtilsData.generateAboutAuthorData()
    fun getAboutApplication(): List<About> = UtilsData.generateAboutApplicationData()
}