package com.taufik.themovieshow.data.viewmodel.tvshow

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.taufik.themovieshow.BuildConfig
import com.taufik.themovieshow.data.api.ApiClient
import com.taufik.themovieshow.data.local.dao.FavoriteTvShowDao
import com.taufik.themovieshow.data.local.entity.FavoriteTvShow
import com.taufik.themovieshow.data.local.room.TvShowDatabase
import com.taufik.themovieshow.data.main.tvshow.cast.TvShowsCast
import com.taufik.themovieshow.data.main.tvshow.cast.TvShowsCastResponse
import com.taufik.themovieshow.data.main.tvshow.detail.TvShowsPopularDetailResponse
import com.taufik.themovieshow.data.main.tvshow.video.TvShowsVideoResponse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailTvShowViewModel(application: Application) : AndroidViewModel(application) {

    private val apiKey = BuildConfig.API_KEY
    private val apiInstance = ApiClient.apiInstance

    private val _listDetailTvShows = MutableLiveData<TvShowsPopularDetailResponse>()
    val detailTvShows: LiveData<TvShowsPopularDetailResponse> = _listDetailTvShows

    private val _listDetailVideo = MutableLiveData<TvShowsVideoResponse>()
    val detailVideo: LiveData<TvShowsVideoResponse> = _listDetailVideo

    private val _listDetailCast = MutableLiveData<ArrayList<TvShowsCast>>()
    val listDetailCasts: LiveData<ArrayList<TvShowsCast>> = _listDetailCast

    private var tvShowDao: FavoriteTvShowDao?
    private var tvShowDb: TvShowDatabase? = TvShowDatabase.getDatabase(context = application)

    init {
        tvShowDao = tvShowDb?.favoriteTvShowDao()
    }

    fun setDetailTvShowPopular(id: Int) {
        apiInstance.getDetailTvShows(id, apiKey)
            .enqueue(object : Callback<TvShowsPopularDetailResponse> {
                override fun onResponse(
                    call: Call<TvShowsPopularDetailResponse>,
                    response: Response<TvShowsPopularDetailResponse>
                ) {
                    if (response.isSuccessful) {
                        _listDetailTvShows.value = response.body()
                        Log.e("listDetailTvShows", "onResponse: ${response.body()}")
                    }
                }

                override fun onFailure(call: Call<TvShowsPopularDetailResponse>, t: Throwable) {
                    Log.e("errorRetrofit", "onFailure: ${t.localizedMessage}")
                }
            })
    }

    fun setDetailTvShowVideo(id: Int) {
        apiInstance.getTvShowsVideo(id, apiKey)
            .enqueue(object : Callback<TvShowsVideoResponse> {
                override fun onResponse(
                    call: Call<TvShowsVideoResponse>,
                    response: Response<TvShowsVideoResponse>
                ) {
                    if (response.isSuccessful) {
                        _listDetailVideo.value = response.body()
                        Log.e("listDetailVideo", "onResponse: ${response.body()}")
                    }
                }

                override fun onFailure(call: Call<TvShowsVideoResponse>, t: Throwable) {
                    Log.e("errorRetrofit", "onFailure: ${t.localizedMessage}")
                }
            })
    }

    fun setDetailTvShowsCast(id: Int) {
        apiInstance.getTvShowsCast(id, apiKey)
            .enqueue(object : Callback<TvShowsCastResponse> {
                override fun onResponse(
                    call: Call<TvShowsCastResponse>,
                    response: Response<TvShowsCastResponse>
                ) {
                    if (response.isSuccessful) {
                        _listDetailCast.value = response.body()?.cast as ArrayList<TvShowsCast>
                        Log.e("listDetailCast", "onResponse: ${response.body()}")
                    }
                }

                override fun onFailure(call: Call<TvShowsCastResponse>, t: Throwable) {
                    Log.e("errorRetrofit", "onFailure: ${t.localizedMessage}")
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