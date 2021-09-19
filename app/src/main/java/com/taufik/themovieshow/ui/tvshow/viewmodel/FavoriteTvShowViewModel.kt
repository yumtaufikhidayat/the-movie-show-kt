package com.taufik.themovieshow.ui.tvshow.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.taufik.themovieshow.ui.favorite.data.tvshow.FavoriteTvShow
import com.taufik.themovieshow.ui.favorite.data.tvshow.FavoriteTvShowDao
import com.taufik.themovieshow.ui.favorite.data.tvshow.TvShowDatabase

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