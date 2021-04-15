package com.taufik.themovieshow.ui.feature.tvshow.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.taufik.themovieshow.ui.feature.tvshow.data.local.FavoriteTvShow
import com.taufik.themovieshow.ui.feature.tvshow.data.local.FavoriteTvShowDao
import com.taufik.themovieshow.ui.feature.tvshow.data.local.TvShowDatabase

class FavoriteTvShowViewModel(application: Application) : AndroidViewModel(application) {

    private var tvShowDao: FavoriteTvShowDao?
    private var tvShowDb: TvShowDatabase? = TvShowDatabase.getDatabase(context = application)

    init {
        tvShowDao = tvShowDb?.favoriteTvShowDao()
    }

    fun getFavoriteTvShow(): LiveData<List<FavoriteTvShow>>? {
        return tvShowDao?.getFavoriteTvShow()
    }
}