package com.taufik.themovieshow.ui.about.viewmodel

import androidx.lifecycle.ViewModel
import com.taufik.themovieshow.data.repository.TheMovieShowRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AboutViewModel @Inject constructor(private val repository: TheMovieShowRepository): ViewModel() {
    fun getAboutData() = repository.getAboutData()
}