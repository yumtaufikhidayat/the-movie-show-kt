package com.taufik.themovieshow.model.response.movie.detail


import com.google.gson.annotations.SerializedName

data class MovieDetailResponse(
    @SerializedName("adult")
    val adult: Boolean,
    @SerializedName("backdrop_path")
    val backdropPath: String,
    @SerializedName("belongs_to_collection")
    val belongsToCollection: com.taufik.themovieshow.model.response.movie.detail.MovieDetailBelongsToCollection,
    @SerializedName("budget")
    val budget: Int,
    @SerializedName("genres")
    val genres: List<com.taufik.themovieshow.model.response.movie.detail.MovieDetailGenre>,
    @SerializedName("homepage")
    val homepage: String,
    @SerializedName("id")
    val id: Int,
    @SerializedName("imdb_id")
    val imdbId: String,
    @SerializedName("original_language")
    val originalLanguage: String,
    @SerializedName("original_title")
    val originalTitle: String,
    @SerializedName("overview")
    val overview: String,
    @SerializedName("popularity")
    val popularity: Double,
    @SerializedName("poster_path")
    val posterPath: String,
    @SerializedName("production_companies")
    val productionCompanies: List<com.taufik.themovieshow.model.response.movie.detail.MovieDetailProductionCompany>,
    @SerializedName("production_countries")
    val productionCountries: List<com.taufik.themovieshow.model.response.movie.detail.MovieDetailProductionCountry>,
    @SerializedName("release_date")
    val releaseDate: String,
    @SerializedName("revenue")
    val revenue: Int,
    @SerializedName("runtime")
    val runtime: Int,
    @SerializedName("spoken_languages")
    val spokenLanguages: List<com.taufik.themovieshow.model.response.movie.detail.MovieDetailSpokenLanguage>,
    @SerializedName("status")
    val status: String,
    @SerializedName("tagline")
    val tagline: String,
    @SerializedName("title")
    val title: String,
    @SerializedName("video")
    val video: Boolean,
    @SerializedName("vote_average")
    val voteAverage: Double,
    @SerializedName("vote_count")
    val voteCount: Int
)