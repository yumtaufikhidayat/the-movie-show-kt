package com.taufik.themovieshow.utils.extensions

import com.taufik.themovieshow.base.helper.BaseVideoItem
import com.taufik.themovieshow.base.helper.BaseVideoItemImpl
import com.taufik.themovieshow.model.response.movie.video.MovieVideoResult
import com.taufik.themovieshow.model.response.tvshow.video.TvShowsVideoResult


fun List<MovieVideoResult>?.toMovieBaseVideoItemList(): List<BaseVideoItem> =
    this.orEmpty().map { BaseVideoItemImpl(it.id, it.name, it.key) }


fun List<TvShowsVideoResult>?.toTvShowBaseVideoItemList(): List<BaseVideoItem> =
    this.orEmpty().map { BaseVideoItemImpl(it.id, it.name, it.key) }