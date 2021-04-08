package com.taufik.themovieshow.ui.main.movie.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.taufik.themovieshow.api.ApiClient
import com.taufik.themovieshow.ui.main.movie.data.main.MovieMainResponse
import com.taufik.themovieshow.ui.main.movie.data.main.MovieMainResult
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MovieViewModel : ViewModel() {

    private val listNowPlaying = MutableLiveData<ArrayList<MovieMainResult>>()

    fun setMovieNowPlaying(apiKey: String){
        ApiClient.apiInstance
                .getMovieNowPlaying(apiKey)
                .enqueue(object : Callback<MovieMainResponse> {
                    override fun onResponse(
                            call: Call<MovieMainResponse>,
                            response: Response<MovieMainResponse>
                    ) {
                        if (response.isSuccessful) {
                            listNowPlaying.postValue(response.body()?.results as ArrayList<MovieMainResult>)
                            Log.e("mainSuccess", "onResponse: ${response.body()}")
                        }
                    }

                    override fun onFailure(call: Call<MovieMainResponse>, t: Throwable) {
                        Log.e("mainFailed", "onFailure: ${t.localizedMessage}")
                    }
                })
    }

    fun getMovieNowPlaying(): LiveData<ArrayList<MovieMainResult>> {
        return listNowPlaying
    }
}