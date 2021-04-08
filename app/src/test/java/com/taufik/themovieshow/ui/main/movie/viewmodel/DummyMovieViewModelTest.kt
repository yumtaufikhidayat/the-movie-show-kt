package com.taufik.themovieshow.ui.main.movie.viewmodel

import org.junit.Test

import org.junit.Assert.*
import org.junit.Before

class DummyMovieViewModelTest {

    private lateinit var viewModel: DummyMovieViewModel

    @Before
    fun setUp() {
        viewModel = DummyMovieViewModel()
    }

    @Test
    fun getMovieNowPlaying() {
        val listNowPlaying = viewModel.getMovieNowPlaying()
        assertNotNull(listNowPlaying)
        assertEquals(20, listNowPlaying.size)
    }

    @Test
    fun getMovieUpcoming() {
        val listUpcoming = viewModel.getMovieUpcoming()
        assertNotNull(listUpcoming)
        assertEquals(20, listUpcoming.size)
    }
}