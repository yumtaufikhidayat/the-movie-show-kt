package com.taufik.themovieshow.ui.feature.about.viewmodel

import androidx.lifecycle.ViewModel
import com.taufik.themovieshow.ui.feature.about.data.About
import com.taufik.themovieshow.utils.UtilsData

class AboutViewModel : ViewModel() {

    fun getAbouts(): List<About> = UtilsData.generateAboutData()
}