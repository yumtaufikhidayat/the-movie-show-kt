package com.taufik.themovieshow.ui.main.tvshow.viewmodel

import org.junit.Test

import org.junit.Assert.*
import org.junit.Before

class DummyTvShowsViewModelTest {

    private lateinit var viewModel: DummyTvShowsViewModel

    @Before
    fun setUp() {
        viewModel = DummyTvShowsViewModel()
    }

    @Test
    fun getTvShowsPopular() {
        val listPopular = viewModel.getTvShowsPopular()
        assertNotNull(listPopular)
        assertEquals(20, listPopular.size)
    }

    @Test
    fun getTvShowsAiringToday() {
        val listAiringToday = viewModel.getTvShowsAiringToday()
        assertNotNull(listAiringToday)
        assertEquals(20, listAiringToday.size)
    }
}