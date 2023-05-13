package com.taufik.themovieshow.data.paging.movie

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.taufik.themovieshow.data.remote.api.ApiService
import com.taufik.themovieshow.model.response.movie.trending.MovieTrendingResult
import com.taufik.themovieshow.utils.CommonConstants
import com.taufik.themovieshow.utils.CommonConstants.STARTING_PAGE_INDEX
import retrofit2.HttpException

class MovieTrendingPagingSource(
    private val apiService: ApiService
) : PagingSource<Int, MovieTrendingResult>() {
    override fun getRefreshKey(state: PagingState<Int, MovieTrendingResult>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, MovieTrendingResult> {
        return try {
            val currentPage = params.key ?: STARTING_PAGE_INDEX
            val response = apiService.getMovieTrendingDay(currentPage)
            val data = response.body()?.results
            val nextKey = if (data.isNullOrEmpty()) {
                null
            } else {
                currentPage + (params.loadSize / CommonConstants.LOAD_MAX_PER_PAGE)
            }
            LoadResult.Page(
                data = data ?: emptyList(),
                prevKey = if (currentPage == STARTING_PAGE_INDEX) null else currentPage - 1,
                nextKey = nextKey?.plus(1)
            )
        } catch (httpEx: HttpException) {
            FirebaseCrashlytics.getInstance().recordException(httpEx)
            LoadResult.Error(httpEx)
        } catch (ex: Exception) {
            FirebaseCrashlytics.getInstance().recordException(ex)
            LoadResult.Error(ex)
        }
    }
}