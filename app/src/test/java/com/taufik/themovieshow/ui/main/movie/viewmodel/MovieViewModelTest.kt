package com.taufik.themovieshow.ui.main.movie.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.taufik.themovieshow.BuildConfig
import com.taufik.themovieshow.ui.main.movie.data.main.MovieResult
import junit.framework.TestCase.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.mockito.Mock
import org.mockito.MockitoAnnotations

class MovieViewModelTest {

    @Mock
    private lateinit var viewModel: MovieViewModel

    @Mock
    private lateinit var observer: Observer<ArrayList<MovieResult>>

    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        viewModel = MovieViewModel()
        viewModel.getMovieNowPlaying().observeForever(observer)
    }

    @Test
    fun setMovieNowPlaying() {
        viewModel.setMovieNowPlaying(BuildConfig.API_KEY)
    }

    @Test
    fun getMovieNowPlaying() {
        assertNotNull(viewModel.getMovieNowPlaying())
        assertEquals("20", viewModel.getMovieNowPlaying().value)
    }

    @Test
    fun setMovieUpcoming() {
        viewModel.setMovieUpcoming(BuildConfig.API_KEY)
    }

    @Test
    fun getMovieUpcoming() {
    }
}