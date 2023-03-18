package com.taufik.themovieshow.data.viewmodel.tvshow

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.taufik.themovieshow.BuildConfig
import com.taufik.themovieshow.data.local.dao.FavoriteTvShowDao
import com.taufik.themovieshow.data.local.entity.FavoriteTvShow
import com.taufik.themovieshow.data.local.room.TvShowDatabase
import com.taufik.themovieshow.data.remote.api.ApiClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailTvShowViewModel(application: Application) : AndroidViewModel(application) {

    private val apiKey = BuildConfig.API_KEY
    private val apiInstance = ApiClient.apiInstance

    private val _listDetailTvShows =
        MutableLiveData<com.taufik.themovieshow.model.response.tvshow.detail.TvShowsPopularDetailResponse>()
    val detailTvShows: LiveData<com.taufik.themovieshow.model.response.tvshow.detail.TvShowsPopularDetailResponse> =
        _listDetailTvShows

    private val _listDetailVideo =
        MutableLiveData<com.taufik.themovieshow.model.response.tvshow.video.TvShowsVideoResponse>()
    val detailVideo: LiveData<com.taufik.themovieshow.model.response.tvshow.video.TvShowsVideoResponse> =
        _listDetailVideo

    private val _listDetailCast =
        MutableLiveData<ArrayList<com.taufik.themovieshow.model.response.tvshow.cast.TvShowsCast>>()
    val listDetailCasts: LiveData<ArrayList<com.taufik.themovieshow.model.response.tvshow.cast.TvShowsCast>> =
        _listDetailCast

    private val _listReviews =
        MutableLiveData<ArrayList<com.taufik.themovieshow.model.response.common.reviews.ReviewsResult>>()
    val listReviewTvShows: LiveData<ArrayList<com.taufik.themovieshow.model.response.common.reviews.ReviewsResult>> =
        _listReviews

    private val _listSimilar =
        MutableLiveData<ArrayList<com.taufik.themovieshow.model.response.tvshow.similar.TvShowsSimilarResultsItem>>()
    val listSimilarTvShows: LiveData<ArrayList<com.taufik.themovieshow.model.response.tvshow.similar.TvShowsSimilarResultsItem>> =
        _listSimilar

    private var tvShowDao: FavoriteTvShowDao?
    private var tvShowDb: TvShowDatabase? = TvShowDatabase.getDatabase(context = application)

    init {
        tvShowDao = tvShowDb?.favoriteTvShowDao()
    }

    fun setDetailTvShowPopular(id: Int) {
        apiInstance.getDetailTvShows(id, apiKey)
            .enqueue(object :
                Callback<com.taufik.themovieshow.model.response.tvshow.detail.TvShowsPopularDetailResponse> {
                override fun onResponse(
                    call: Call<com.taufik.themovieshow.model.response.tvshow.detail.TvShowsPopularDetailResponse>,
                    response: Response<com.taufik.themovieshow.model.response.tvshow.detail.TvShowsPopularDetailResponse>
                ) {
                    if (response.isSuccessful) {
                        _listDetailTvShows.value = response.body()
                    }
                }

                override fun onFailure(
                    call: Call<com.taufik.themovieshow.model.response.tvshow.detail.TvShowsPopularDetailResponse>,
                    t: Throwable
                ) {
                }
            })
    }

    fun setDetailTvShowVideo(id: Int) {
        apiInstance.getTvShowsVideo(id, apiKey)
            .enqueue(object :
                Callback<com.taufik.themovieshow.model.response.tvshow.video.TvShowsVideoResponse> {
                override fun onResponse(
                    call: Call<com.taufik.themovieshow.model.response.tvshow.video.TvShowsVideoResponse>,
                    response: Response<com.taufik.themovieshow.model.response.tvshow.video.TvShowsVideoResponse>
                ) {
                    if (response.isSuccessful) {
                        _listDetailVideo.value = response.body()
                    }
                }

                override fun onFailure(
                    call: Call<com.taufik.themovieshow.model.response.tvshow.video.TvShowsVideoResponse>,
                    t: Throwable
                ) {
                }
            })
    }

    fun setDetailTvShowsCast(id: Int) {
        apiInstance.getTvShowsCast(id, apiKey)
            .enqueue(object :
                Callback<com.taufik.themovieshow.model.response.tvshow.cast.TvShowsCastResponse> {
                override fun onResponse(
                    call: Call<com.taufik.themovieshow.model.response.tvshow.cast.TvShowsCastResponse>,
                    response: Response<com.taufik.themovieshow.model.response.tvshow.cast.TvShowsCastResponse>
                ) {
                    if (response.isSuccessful) {
                        _listDetailCast.value =
                            response.body()?.cast as ArrayList<com.taufik.themovieshow.model.response.tvshow.cast.TvShowsCast>
                    }
                }

                override fun onFailure(
                    call: Call<com.taufik.themovieshow.model.response.tvshow.cast.TvShowsCastResponse>,
                    t: Throwable
                ) {
                }
            })
    }

    fun setDetailTvShowsReviews(id: Int) {
        apiInstance.getReviewsTvShows(id, apiKey)
            .enqueue(object :
                Callback<com.taufik.themovieshow.model.response.common.reviews.ReviewsResponse> {
                override fun onResponse(
                    call: Call<com.taufik.themovieshow.model.response.common.reviews.ReviewsResponse>,
                    response: Response<com.taufik.themovieshow.model.response.common.reviews.ReviewsResponse>
                ) {
                    if (response.isSuccessful) {
                        _listReviews.value =
                            response.body()?.results as ArrayList<com.taufik.themovieshow.model.response.common.reviews.ReviewsResult>
                    }
                }

                override fun onFailure(
                    call: Call<com.taufik.themovieshow.model.response.common.reviews.ReviewsResponse>,
                    t: Throwable
                ) {
                }
            })
    }

    fun setDetailTvShowsSimilar(id: Int) {
        apiInstance.getSimilarTvShows(id, apiKey)
            .enqueue(object :
                Callback<com.taufik.themovieshow.model.response.tvshow.similar.TvShowsSimilarResponse> {
                override fun onResponse(
                    call: Call<com.taufik.themovieshow.model.response.tvshow.similar.TvShowsSimilarResponse>,
                    response: Response<com.taufik.themovieshow.model.response.tvshow.similar.TvShowsSimilarResponse>
                ) {
                    if (response.isSuccessful) {
                        _listSimilar.value =
                            response.body()?.results as ArrayList<com.taufik.themovieshow.model.response.tvshow.similar.TvShowsSimilarResultsItem>
                    }
                }

                override fun onFailure(
                    call: Call<com.taufik.themovieshow.model.response.tvshow.similar.TvShowsSimilarResponse>,
                    t: Throwable
                ) {
                }
            })
    }

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