package com.taufik.themovieshow.data.viewmodel.about

import androidx.lifecycle.ViewModel
import com.taufik.themovieshow.utils.UtilsData

class AboutViewModel : ViewModel() {
    fun getAboutAuthor(): List<com.taufik.themovieshow.model.response.about.About> =
        UtilsData.generateAboutAuthorData()

    fun getAboutApplication(): List<com.taufik.themovieshow.model.response.about.About> =
        UtilsData.generateAboutApplicationData()
}