package com.taufik.themovieshow.base.helper

import com.taufik.themovieshow.model.response.movie.detail.MovieDetailResponse
import com.taufik.themovieshow.model.response.tvshow.detail.TvShowsPopularDetailResponse

interface BaseDetailData {
    val id: Int
    val posterPath: String?
    val backdropPath: String?
    val titleText: String
    val releaseDateText: String
    val statusText: String
    val voteAverage: Double
    val voteCount: Int
    val isAdult: Boolean
    val spokenLanguages: List<String>
    val genres: List<String>
    val overview: String
    val homepage: String?
}

data class MovieDetailWrapper(val movie: MovieDetailResponse) : BaseDetailData {
    override val id = movie.id
    override val posterPath = movie.posterPath
    override val backdropPath = movie.backdropPath
    override val titleText = movie.title
    override val releaseDateText = movie.releaseDate
    override val statusText = movie.status
    override val voteAverage = movie.voteAverage
    override val voteCount = movie.voteCount
    override val isAdult = movie.adult
    override val spokenLanguages = movie.spokenLanguages.map { it.englishName }
    override val genres = movie.genres.map { it.name }
    override val overview = movie.overview
    override val homepage = movie.homepage
}

data class TvShowDetailWrapper(val tvShow: TvShowsPopularDetailResponse) : BaseDetailData {
    override val id = tvShow.id
    override val posterPath = tvShow.posterPath
    override val backdropPath = tvShow.backdropPath
    override val titleText = tvShow.name
    override val releaseDateText = tvShow.firstAirDate
    override val statusText = tvShow.status
    override val voteAverage = tvShow.voteAverage
    override val voteCount = tvShow.voteCount
    override val isAdult = tvShow.adult
    override val spokenLanguages = tvShow.spokenLanguages.map { it.englishName }
    override val genres = tvShow.genres.map { it.name }
    override val overview = tvShow.overview
    override val homepage = tvShow.homepage
}