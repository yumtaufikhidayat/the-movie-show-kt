package com.taufik.themovieshow.ui.tvshow.viewmodel

import com.taufik.themovieshow.data.NetworkResult
import com.taufik.themovieshow.data.repository.FakeTheMovieShowRepository
import com.taufik.themovieshow.model.response.tvshow.popularairingtoday.TvShowsMainResponse
import com.taufik.themovieshow.model.response.tvshow.trending.TvShowsTrendingResponse
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
class TvShowsViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var fakeRepository: FakeTheMovieShowRepository
    private lateinit var viewModel: TvShowsViewModel

    @Before
    fun setUp() {
        fakeRepository = FakeTheMovieShowRepository()
        viewModel = TvShowsViewModel(fakeRepository)
    }

    @Test
    fun `getTvShowsAiringToday emits initial Loading and then Success`() = runTest {
        val dummyData = TvShowsMainResponse(page = 1, results = emptyList(), totalPages = 1, totalResults = 0)
        fakeRepository.tvShowsAiringTodayFlow = flowOf(NetworkResult.Success(dummyData))

        val stateFlow = viewModel.getTvShowsAiringToday()
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            stateFlow.collect {}
        }

        assertTrue(stateFlow.value is NetworkResult.Success)
        assertEquals(dummyData, (stateFlow.value as NetworkResult.Success).data)
    }

    @Test
    fun `getTvShowsPopular emits Success`() = runTest {
        val dummyData = TvShowsMainResponse(page = 1, results = emptyList(), totalPages = 1, totalResults = 0)
        fakeRepository.tvShowsPopularFlow = flowOf(NetworkResult.Success(dummyData))

        val stateFlow = viewModel.getTvShowsPopular()
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            stateFlow.collect {}
        }

        assertTrue(stateFlow.value is NetworkResult.Success)
        assertEquals(dummyData, (stateFlow.value as NetworkResult.Success).data)
    }

    @Test
    fun `getTvShowsTrending emits Success`() = runTest {
        val dummyData = TvShowsTrendingResponse(page = 1, results = emptyList(), totalPages = 1, totalResults = 0)
        fakeRepository.tvShowsTrendingFlow = flowOf(NetworkResult.Success(dummyData))

        val stateFlow = viewModel.getTvShowsTrending()
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            stateFlow.collect {}
        }

        assertTrue(stateFlow.value is NetworkResult.Success)
        assertEquals(dummyData, (stateFlow.value as NetworkResult.Success).data)
    }

    @Test
    fun `getTvShowsTrending emits Error when repository returns Error`() = runTest {
        val errorMessage = "Network timeout"
        fakeRepository.tvShowsTrendingFlow = flowOf(NetworkResult.Error(errorMessage))

        val stateFlow = viewModel.getTvShowsTrending()
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            stateFlow.collect {}
        }

        assertTrue(stateFlow.value is NetworkResult.Error)
        assertEquals(errorMessage, (stateFlow.value as NetworkResult.Error).message)
    }
}
