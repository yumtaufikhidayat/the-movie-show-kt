package com.taufik.themovieshow.ui.movie.viewmodel

import com.taufik.themovieshow.data.NetworkResult
import com.taufik.themovieshow.data.repository.FakeTheMovieShowRepository
import com.taufik.themovieshow.model.response.movie.nowplayingupcoming.MovieMainResponse
import com.taufik.themovieshow.model.response.movie.trending.MovieTrendingResponse
import com.taufik.themovieshow.utils.MainDispatcherRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class MovieViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var fakeRepository: FakeTheMovieShowRepository
    private lateinit var viewModel: MovieViewModel

    @Before
    fun setUp() {
        fakeRepository = FakeTheMovieShowRepository()
        viewModel = MovieViewModel(fakeRepository)
    }

    @Test
    fun `getMovieTrendingToday emits initial Loading and then Success`() = runTest {
        val dummyData = MovieTrendingResponse(page = 1, results = emptyList(), totalPages = 1, totalResults = 0)
        fakeRepository.movieTrendingDayFlow = flowOf(NetworkResult.Success(dummyData))

        val stateFlow = viewModel.getMovieTrendingToday()
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            stateFlow.collect {}
        }

        assertTrue(stateFlow.value is NetworkResult.Success)
        assertEquals(dummyData, (stateFlow.value as NetworkResult.Success).data)
    }

    @Test
    fun `getMovieTrendingToday emits Error when repository returns Error`() = runTest {
        val errorMessage = "Failed to load trending movies"
        fakeRepository.movieTrendingDayFlow = flowOf(NetworkResult.Error(errorMessage))

        val stateFlow = viewModel.getMovieTrendingToday()
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            stateFlow.collect {}
        }

        assertTrue(stateFlow.value is NetworkResult.Error)
        assertEquals(errorMessage, (stateFlow.value as NetworkResult.Error).message)
    }

    @Test
    fun `getMovieNowPlaying emits Success`() = runTest {
        val dummyData = MovieMainResponse(dates = null, page = 1, results = emptyList(), totalPages = 1, totalResults = 0)
        fakeRepository.movieNowPlayingFlow = flowOf(NetworkResult.Success(dummyData))

        val stateFlow = viewModel.getMovieNowPlaying()
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            stateFlow.collect {}
        }

        assertTrue(stateFlow.value is NetworkResult.Success)
        assertEquals(dummyData, (stateFlow.value as NetworkResult.Success).data)
    }

    @Test
    fun `getMovieUpcoming emits Success`() = runTest {
        val dummyData = MovieMainResponse(dates = null, page = 1, results = emptyList(), totalPages = 1, totalResults = 0)
        fakeRepository.movieUpcomingFlow = flowOf(NetworkResult.Success(dummyData))

        val stateFlow = viewModel.getMovieUpcoming()
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            stateFlow.collect {}
        }

        assertTrue(stateFlow.value is NetworkResult.Success)
        assertEquals(dummyData, (stateFlow.value as NetworkResult.Success).data)
    }
}
