package com.taufik.themovieshow.ui.tvshow.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.taufik.themovieshow.data.NetworkResult
import com.taufik.themovieshow.data.local.entity.tvshow.FavoriteTvShowEntity
import com.taufik.themovieshow.data.repository.TheMovieShowRepository
import com.taufik.themovieshow.model.response.common.reviews.ReviewsResponse
import com.taufik.themovieshow.model.response.tvshow.cast.TvShowsCastResponse
import com.taufik.themovieshow.model.response.tvshow.detail.TvShowsPopularDetailResponse
import com.taufik.themovieshow.model.response.tvshow.similar.TvShowsSimilarResponse
import com.taufik.themovieshow.model.response.tvshow.video.TvShowsVideoResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailTvShowViewModel @Inject constructor(private val repository: TheMovieShowRepository) : ViewModel() {

    private val _detailTvShowPopularResponse: MutableLiveData<NetworkResult<TvShowsPopularDetailResponse>> = MutableLiveData()
    val detailTvShowPopularResponse: LiveData<NetworkResult<TvShowsPopularDetailResponse>> = _detailTvShowPopularResponse

    private val _detailTvShowVideoResponse: MutableLiveData<NetworkResult<TvShowsVideoResponse>> = MutableLiveData()
    val detailTvShowVideoResponse: LiveData<NetworkResult<TvShowsVideoResponse>> = _detailTvShowVideoResponse

    private val _detailTvShowCastResponse: MutableLiveData<NetworkResult<TvShowsCastResponse>> = MutableLiveData()
    val detailTvShowCastResponse: LiveData<NetworkResult<TvShowsCastResponse>> = _detailTvShowCastResponse

    private val _detailTvShowReviewsResponse: MutableLiveData<NetworkResult<ReviewsResponse>> = MutableLiveData()
    val detailTvShowReviewsResponse: LiveData<NetworkResult<ReviewsResponse>> = _detailTvShowReviewsResponse

    private val _detailTvShowSimilarResponse: MutableLiveData<NetworkResult<TvShowsSimilarResponse>> = MutableLiveData()
    val detailTvShowSimilarResponse: LiveData<NetworkResult<TvShowsSimilarResponse>> = _detailTvShowSimilarResponse

    fun setDetailTvShowPopular(id: Int) = viewModelScope.launch {
        repository.getDetailTvShows(id).collect {
            _detailTvShowPopularResponse.value = it
        }
    }

    fun setDetailTvShowVideo(id: Int) = viewModelScope.launch {
        repository.getTvShowsVideo(id).collect {
            _detailTvShowVideoResponse.value = it
        }
    }

    fun setDetailTvShowsCast(id: Int) = viewModelScope.launch {
        repository.getTvShowsCast(id).collect {
            _detailTvShowCastResponse.value = it
        }
    }

    fun setDetailTvShowsReviews(id: Int) = viewModelScope.launch {
        repository.getTvShowsReviews(id).collect {
            _detailTvShowReviewsResponse.value = it
        }
    }

    fun setDetailTvShowsSimilar(id: Int) = viewModelScope.launch {
        repository.getSimilarTvShows(id).collect {
            _detailTvShowSimilarResponse.value = it
        }
    }

    // Favorite
    fun addTvShowFavorite(
        tvShowId: Int,
        posterPath: String,
        title: String,
        firstAirDate: String,
        rating: Double
    ) {
        viewModelScope.launch {
            repository.addTvShowToFavorite(
                FavoriteTvShowEntity(
                    tvShowId, posterPath, title, firstAirDate, rating
                )
            )
        }
    }

    suspend fun checkFavoriteTvShow(tvShowId: Int) = repository.checkFavoriteTvShow(tvShowId)

    fun removeTvShowFromFavorite(tvShowId: Int) {
        viewModelScope.launch {
            repository.removeTvShowFromFavorite(tvShowId)
        }
    }
}