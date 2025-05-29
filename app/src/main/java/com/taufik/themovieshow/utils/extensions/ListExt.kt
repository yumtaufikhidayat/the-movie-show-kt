package com.taufik.themovieshow.utils.extensions

import com.taufik.themovieshow.base.helper.BaseVideoItem
import com.taufik.themovieshow.base.helper.toMovieVideoResultItem
import com.taufik.themovieshow.base.helper.toTvShowsVideoResultItem
import com.taufik.themovieshow.model.response.movie.video.MovieVideoResult
import com.taufik.themovieshow.model.response.tvshow.video.TvShowsVideoResult


fun List<MovieVideoResult>?.toMovieBaseVideoItemList(): List<BaseVideoItem> =
    this.orEmpty().map { it.toMovieVideoResultItem() }


fun List<TvShowsVideoResult>?.toTvShowBaseVideoItemList(): List<BaseVideoItem> =
    this.orEmpty().map { it.toTvShowsVideoResultItem() }