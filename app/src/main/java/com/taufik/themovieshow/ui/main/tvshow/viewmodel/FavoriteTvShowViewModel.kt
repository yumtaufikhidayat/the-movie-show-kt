package com.taufik.themovieshow.ui.main.tvshow.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.taufik.themovieshow.data.local.dao.FavoriteTvShowDao
import com.taufik.themovieshow.data.local.entity.FavoriteTvShow
import com.taufik.themovieshow.data.local.room.TvShowDatabase

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