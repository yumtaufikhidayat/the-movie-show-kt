package com.taufik.themovieshow.ui.main.tvshow.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.taufik.themovieshow.api.ApiClient
import com.taufik.themovieshow.ui.main.tvshow.data.tvshowdetail.TvShowsPopularDetailResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailTvShowViewModel : ViewModel() {

    val listDetailTvShowsPopular = MutableLiveData<TvShowsPopularDetailResponse>()

    fun setDetailTvShowPopular(id: Int, apiKey: String) {
        ApiClient.apiInstance
                .getDetailTvShowsPopular(id, apiKey)
                .enqueue(object : Callback<TvShowsPopularDetailResponse> {
                    override fun onResponse(call: Call<TvShowsPopularDetailResponse>, response: Response<TvShowsPopularDetailResponse>) {
                        if (response.isSuccessful) {
                            listDetailTvShowsPopular.postValue(response.body())
                            Log.e("listDetailTvShows", "onResponse: ${response.body()}")
                        }
                    }

                    override fun onFailure(call: Call<TvShowsPopularDetailResponse>, t: Throwable) {
                        Log.e("errorRetrofit", "onFailure: ${t.localizedMessage}")
                    }
                })
    }

    fun getDetailTvShowsPopular(): LiveData<TvShowsPopularDetailResponse> {
        return listDetailTvShowsPopular
    }
}