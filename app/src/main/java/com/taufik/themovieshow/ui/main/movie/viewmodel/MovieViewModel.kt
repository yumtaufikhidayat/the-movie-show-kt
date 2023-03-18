package com.taufik.themovieshow.ui.main.movie.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.taufik.themovieshow.data.repository.TheMovieShowRepository
import com.taufik.themovieshow.model.response.movie.discover.DiscoverMovieResult
import com.taufik.themovieshow.model.response.movie.nowplayingupcoming.MovieMainResult
import com.taufik.themovieshow.model.response.movie.trending.MovieTrendingResult
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MovieViewModel @Inject constructor(private val repository: TheMovieShowRepository) : ViewModel() {

    private val _listNowPlaying = MutableLiveData<ArrayList<MovieMainResult>>()
    val listNowPlaying: LiveData<ArrayList<MovieMainResult>> = _listNowPlaying

    private val _listUpcoming = MutableLiveData<ArrayList<MovieMainResult>>()
    val listUpcoming: LiveData<ArrayList<MovieMainResult>> = _listUpcoming

    private val _listTrendingDay = MutableLiveData<ArrayList<MovieTrendingResult>>()
    val listTrendingDay: LiveData<ArrayList<MovieTrendingResult>> = _listTrendingDay

    private val _listDiscover = MutableLiveData<ArrayList<DiscoverMovieResult>>()
    val listDiscover: LiveData<ArrayList<DiscoverMovieResult>> = _listDiscover

    suspend fun setMovieNowPlaying() = repository.getMovieNowPlaying()

    suspend fun setMovieUpcoming() = repository.getMovieUpcoming()

    suspend fun setMovieTrendingDay() = repository.getMovieTrendingDay()

    suspend fun setDiscoverMovie(query: String) = repository.getDiscoverMovie(query)
}