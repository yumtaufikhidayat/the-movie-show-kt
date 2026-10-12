package com.taufik.themovieshow.base.helper

import com.taufik.themovieshow.model.response.movie.detail.MovieDetailResponse
import com.taufik.themovieshow.model.response.movie.detail.ReleaseDatesResponse
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
    val releaseDates: ReleaseDatesResponse?
}

data class MovieDetailWrapper(val movie: MovieDetailResponse) : BaseDetailData {
    override val id = movie.id
    override val posterPath = movie.posterPath
    override val backdropPath = movie.backdropPath
    override val titleText = movie.title.orEmpty()
    override val releaseDateText = movie.releaseDate.orEmpty()
    override val statusText = movie.status.orEmpty()
    override val voteAverage = movie.voteAverage
    override val voteCount = movie.voteCount
    override val isAdult = movie.adult
    override val spokenLanguages = movie.spokenLanguages.orEmpty().mapNotNull { it.englishName }
    override val genres = movie.genres.orEmpty().mapNotNull { it.name }
    override val overview = movie.overview.orEmpty()
    override val homepage = movie.homepage
    override val releaseDates = movie.releaseDates
}

data class TvShowDetailWrapper(val tvShow: TvShowsPopularDetailResponse) : BaseDetailData {
    override val id = tvShow.id
    override val posterPath = tvShow.posterPath
    override val backdropPath = tvShow.backdropPath
    override val titleText = tvShow.name.orEmpty()
    override val releaseDateText = tvShow.firstAirDate.orEmpty()
    override val statusText = tvShow.status.orEmpty()
    override val voteAverage = tvShow.voteAverage
    override val voteCount = tvShow.voteCount
    override val isAdult = tvShow.adult
    override val spokenLanguages = tvShow.spokenLanguages.orEmpty().mapNotNull { it.englishName }
    override val genres = tvShow.genres.orEmpty().mapNotNull { it.name }
    override val overview = tvShow.overview.orEmpty()
    override val homepage = tvShow.homepage
    override val releaseDates: ReleaseDatesResponse? = null
}