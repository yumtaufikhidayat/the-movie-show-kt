package com.taufik.themovieshow.ui.detail.movie_tvshow.viewmodel

import com.google.gson.Gson
import com.taufik.themovieshow.data.NetworkResult
import com.taufik.themovieshow.data.local.entity.movie.FavoriteMovieEntity
import com.taufik.themovieshow.data.local.entity.tvshow.FavoriteTvShowEntity
import com.taufik.themovieshow.data.repository.FakeTheMovieShowRepository
import com.taufik.themovieshow.model.response.common.reviews.ReviewsResponse
import com.taufik.themovieshow.model.response.movie.cast.MovieCastResponse
import com.taufik.themovieshow.model.response.movie.detail.MovieDetailResponse
import com.taufik.themovieshow.model.response.movie.similar.MovieSimilarResponse
import com.taufik.themovieshow.model.response.movie.video.MovieVideoResponse
import com.taufik.themovieshow.model.response.tvshow.cast.TvShowsCastResponse
import com.taufik.themovieshow.model.response.tvshow.detail.TvShowsPopularDetailResponse
import com.taufik.themovieshow.model.response.tvshow.similar.TvShowsSimilarResponse
import com.taufik.themovieshow.model.response.tvshow.video.TvShowsVideoResponse
import com.taufik.themovieshow.utils.MainDispatcherRule
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertSame
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class DetailMovieTvShowViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var repository: FakeTheMovieShowRepository
    private lateinit var viewModel: DetailMovieTvShowViewModel

    @Before
    fun setUp() {
        repository = FakeTheMovieShowRepository()
        viewModel = DetailMovieTvShowViewModel(repository)
    }

    private fun <T> success(data: T): Flow<NetworkResult<T>> = flowOf(NetworkResult.Success(data))

    private fun <T> TestScope.collected(flow: StateFlow<NetworkResult<T>>): NetworkResult<T> {
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) { flow.collect {} }
        return flow.value
    }

    @Test
    fun `movie detail responses expose repository data`() = runTest {
        val detail = Gson().fromJson("""{"id": 42}""", MovieDetailResponse::class.java)
        val cast = MovieCastResponse(cast = emptyList(), crew = emptyList(), id = 42)
        val video = MovieVideoResponse(id = 42, results = emptyList())
        val reviews = ReviewsResponse(id = 42, page = 1, results = emptyList(), totalPages = 1, totalResults = 0)
        val similar = MovieSimilarResponse(page = 1, results = emptyList(), totalPages = 1, totalResults = 0)
        repository.detailMovieFlow = success(detail)
        repository.movieCastFlow = success(cast)
        repository.movieVideoFlow = success(video)
        repository.movieReviewsFlow = success(reviews)
        repository.similarMovieFlow = success(similar)

        assertSame(detail, collected(viewModel.detailMoviesResponse(42)).data)
        assertSame(cast, collected(viewModel.detailMoviesCastResponse(42)).data)
        assertSame(video, collected(viewModel.detailMoviesVideoResponse(42)).data)
        assertSame(reviews, collected(viewModel.detailMovieReviewsResponse(42)).data)
        assertSame(similar, collected(viewModel.detailMovieSimilarResponse(42)).data)
    }

    @Test
    fun `tv show detail responses expose repository data`() = runTest {
        val detail = Gson().fromJson("""{"id": 7}""", TvShowsPopularDetailResponse::class.java)
        val cast = TvShowsCastResponse(cast = emptyList(), crew = emptyList(), id = 7)
        val video = TvShowsVideoResponse(id = 7, results = emptyList())
        val reviews = ReviewsResponse(id = 7, page = 1, results = emptyList(), totalPages = 1, totalResults = 0)
        val similar = TvShowsSimilarResponse(page = 1, totalPages = 1, results = emptyList(), totalResults = 0)
        repository.detailTvShowsFlow = success(detail)
        repository.tvShowsCastFlow = success(cast)
        repository.tvShowsVideoFlow = success(video)
        repository.tvShowsReviewsFlow = success(reviews)
        repository.similarTvShowsFlow = success(similar)

        assertSame(detail, collected(viewModel.detailTvShowResponse(7)).data)
        assertSame(cast, collected(viewModel.detailTvShowCastResponse(7)).data)
        assertSame(video, collected(viewModel.detailTvShowVideoResponse(7)).data)
        assertSame(reviews, collected(viewModel.detailTvShowReviewsResponse(7)).data)
        assertSame(similar, collected(viewModel.detailTvShowSimilarResponse(7)).data)
    }

    @Test
    fun `detail error is passed through`() = runTest {
        repository.detailMovieFlow = flowOf(NetworkResult.Error("Not found"))

        assertEquals("Not found", collected(viewModel.detailMoviesResponse(1)).message)
    }

    @Test
    fun `adding a movie saves it as favorite entity`() = runTest {
        viewModel.addMovieToFavorite(42, "/poster.jpg", "Inception", "2010-07-16", 8.4)

        assertEquals(
            listOf(FavoriteMovieEntity(42, "/poster.jpg", "Inception", "2010-07-16", 8.4)),
            repository.favoriteMovies
        )
        assertEquals(1, viewModel.checkFavoriteMovie(42))
    }

    @Test
    fun `removing a movie clears its favorite status`() = runTest {
        viewModel.addMovieToFavorite(42, "/poster.jpg", "Inception", "2010-07-16", 8.4)

        viewModel.removeMovieFromFavorite(42)

        assertEquals(0, viewModel.checkFavoriteMovie(42))
    }

    @Test
    fun `adding a tv show saves it as favorite entity`() = runTest {
        viewModel.addTvShowFavorite(7, "/poster.jpg", "Dark", "2017-12-01", 8.4)

        assertEquals(
            listOf(FavoriteTvShowEntity(7, "/poster.jpg", "Dark", "2017-12-01", 8.4)),
            repository.favoriteTvShows
        )
        assertEquals(1, viewModel.checkFavoriteTvShow(7))
    }

    @Test
    fun `removing a tv show clears its favorite status`() = runTest {
        viewModel.addTvShowFavorite(7, "/poster.jpg", "Dark", "2017-12-01", 8.4)

        viewModel.removeTvShowFromFavorite(7)

        assertEquals(0, viewModel.checkFavoriteTvShow(7))
    }
}
