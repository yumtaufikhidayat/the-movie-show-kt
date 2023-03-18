package com.taufik.themovieshow.ui.main.tvshow.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.taufik.themovieshow.data.local.dao.FavoriteTvShowDao
import com.taufik.themovieshow.data.local.entity.FavoriteTvShow
import com.taufik.themovieshow.data.local.room.TvShowDatabase
import com.taufik.themovieshow.data.repository.TheMovieShowRepository
import com.taufik.themovieshow.model.response.common.reviews.ReviewsResult
import com.taufik.themovieshow.model.response.tvshow.cast.TvShowsCast
import com.taufik.themovieshow.model.response.tvshow.detail.TvShowsPopularDetailResponse
import com.taufik.themovieshow.model.response.tvshow.similar.TvShowsSimilarResultsItem
import com.taufik.themovieshow.model.response.tvshow.video.TvShowsVideoResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailTvShowViewModel @Inject constructor(
    application: Application,
    private val repository: TheMovieShowRepository
) : AndroidViewModel(application) {

    private val _listDetailTvShows = MutableLiveData<TvShowsPopularDetailResponse>()
    val detailTvShows: LiveData<TvShowsPopularDetailResponse> = _listDetailTvShows

    private val _listDetailVideo = MutableLiveData<TvShowsVideoResponse>()
    val detailVideo: LiveData<TvShowsVideoResponse> = _listDetailVideo

    private val _listDetailCast = MutableLiveData<ArrayList<TvShowsCast>>()
    val listDetailCasts: LiveData<ArrayList<TvShowsCast>> = _listDetailCast

    private val _listReviews = MutableLiveData<ArrayList<ReviewsResult>>()
    val listReviewTvShows: LiveData<ArrayList<ReviewsResult>> = _listReviews

    private val _listSimilar = MutableLiveData<ArrayList<TvShowsSimilarResultsItem>>()
    val listSimilarTvShows: LiveData<ArrayList<TvShowsSimilarResultsItem>> = _listSimilar

    private var tvShowDao: FavoriteTvShowDao?
    private var tvShowDb: TvShowDatabase? = TvShowDatabase.getDatabase(context = application)

    init {
        tvShowDao = tvShowDb?.favoriteTvShowDao()
    }

    suspend fun setDetailTvShowPopular(id: Int) = repository.getDetailTvShows(id)

    suspend fun setDetailTvShowVideo(id: Int) = repository.getTvShowsVideo(id)

    suspend fun setDetailTvShowsCast(id: Int) = repository.getTvShowsCast(id)

    suspend fun setDetailTvShowsReviews(id: Int) = repository.getTvShowsReviews(id)

    suspend fun setDetailTvShowsSimilar(id: Int) = repository.getSimilarTvShows(id)

    // Favorite
    fun addToFavorite(
        tvShowId: Int,
        posterPath: String,
        title: String,
        firstAirDate: String,
        rating: Double
    ) {
        CoroutineScope(Dispatchers.IO).launch {
            val tvShow = FavoriteTvShow(
                tvShowId, posterPath, title, firstAirDate, rating
            )
            tvShowDao?.addToFavorite(tvShow)
        }
    }

    suspend fun checkFavorite(tvShowId: Int) = tvShowDao?.checkFavorite(tvShowId)

    fun removeFromFavorite(tvShowId: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            tvShowDao?.removeFromFavorite(tvShowId)
        }
    }
}