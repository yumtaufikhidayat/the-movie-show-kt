package com.taufik.themovieshow.model.response.tvshow.similar

import com.google.gson.annotations.SerializedName

data class TvShowsSimilarResultsItem(

	@SerializedName("first_air_date")
	val firstAirDate: String,

	@SerializedName("overview")
	val overview: String,

	@SerializedName("original_language")
	val originalLanguage: String,

	@SerializedName("genre_ids")
	val genreIds: List<Int>,

	@SerializedName("poster_path")
	val posterPath: String,

	@SerializedName("origin_country")
	val originCountry: List<String>,

	@SerializedName("backdrop_path")
	val backdropPath: String,

	@SerializedName("original_name")
	val originalName: String,

	@SerializedName("popularity")
	val popularity: Double,

	@SerializedName("vote_average")
	val voteAverage: Double,

	@SerializedName("name")
	val name: String,

	@SerializedName("id")
	val id: Int,

	@SerializedName("vote_count")
	val voteCount: Int
)