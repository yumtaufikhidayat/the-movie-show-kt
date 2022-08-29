package com.taufik.themovieshow.ui.tvshow.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.taufik.themovieshow.BuildConfig
import com.taufik.themovieshow.api.ApiClient
import com.taufik.themovieshow.ui.favorite.data.tvshow.FavoriteTvShow
import com.taufik.themovieshow.ui.favorite.data.tvshow.FavoriteTvShowDao
import com.taufik.themovieshow.ui.favorite.data.tvshow.TvShowDatabase
import com.taufik.themovieshow.ui.tvshow.model.cast.TvShowsCast
import com.taufik.themovieshow.ui.tvshow.model.cast.TvShowsCastResponse
import com.taufik.themovieshow.ui.tvshow.model.detail.TvShowsPopularDetailResponse
import com.taufik.themovieshow.ui.tvshow.model.video.TvShowsVideoResponse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailTvShowViewModel(application: Application) : AndroidViewModel(application) {

    val apiKey = BuildConfig.API_KEY

    val listDetailTvShows = MutableLiveData<TvShowsPopularDetailResponse>()
    val listDetailVideo = MutableLiveData<TvShowsVideoResponse>()
    val listDetailCast = MutableLiveData<ArrayList<TvShowsCast>>()

    private var tvShowDao: FavoriteTvShowDao?
    private var tvShowDb: TvShowDatabase? = TvShowDatabase.getDatabase(context = application)

    init {
        tvShowDao = tvShowDb?.favoriteTvShowDao()
    }

    fun setDetailTvShowPopular(id: Int) {
        ApiClient.apiInstance
                .getDetailTvShows(id, apiKey)
                .enqueue(object : Callback<TvShowsPopularDetailResponse> {
                    override fun onResponse(call: Call<TvShowsPopularDetailResponse>, response: Response<TvShowsPopularDetailResponse>) {
                        if (response.isSuccessful) {
                            listDetailTvShows.postValue(response.body())
                            Log.e("listDetailTvShows", "onResponse: ${response.body()}")
                        }
                    }

                    override fun onFailure(call: Call<TvShowsPopularDetailResponse>, t: Throwable) {
                        Log.e("errorRetrofit", "onFailure: ${t.localizedMessage}")
                    }
                })
    }

    fun getDetailTvShowsPopular(): LiveData<TvShowsPopularDetailResponse> {
        return listDetailTvShows
    }

    fun setDetailTvShowVideo(id: Int) {
        ApiClient.apiInstance
            .getTvShowsVideo(id, apiKey)
            .enqueue(object : Callback<TvShowsVideoResponse> {
                override fun onResponse(
                    call: Call<TvShowsVideoResponse>,
                    response: Response<TvShowsVideoResponse>
                ) {
                    if (response.isSuccessful) {
                        listDetailVideo.postValue(response.body())
                        Log.e("listDetailVideo", "onResponse: ${response.body()}")
                    }
                }

                override fun onFailure(call: Call<TvShowsVideoResponse>, t: Throwable) {
                    Log.e("errorRetrofit", "onFailure: ${t.localizedMessage}")
                }
            })
    }

    fun getDetailTvShowsVideo(): LiveData<TvShowsVideoResponse> {
        return listDetailVideo
    }

    fun setDetailTvShowsCast(id: Int) {
        ApiClient.apiInstance
            .getTvShowsCast(id, apiKey)
            .enqueue(object : Callback<TvShowsCastResponse> {
                override fun onResponse(
                    call: Call<TvShowsCastResponse>,
                    response: Response<TvShowsCastResponse>
                ) {
                    if (response.isSuccessful) {
                        listDetailCast.postValue(response.body()?.cast as ArrayList<TvShowsCast>)
                        Log.e("listDetailCast", "onResponse: ${response.body()}")
                    }
                }

                override fun onFailure(call: Call<TvShowsCastResponse>, t: Throwable) {
                    Log.e("errorRetrofit", "onFailure: ${t.localizedMessage}")
                }
            })
    }

    fun getDetailTvShowsCast(): LiveData<ArrayList<TvShowsCast>> {
        return listDetailCast
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