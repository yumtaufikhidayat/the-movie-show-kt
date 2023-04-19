package com.taufik.themovieshow.data.paging.movie

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.taufik.themovieshow.data.remote.api.ApiService
import com.taufik.themovieshow.model.response.movie.trending.MovieTrendingResult
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
        val currentPage = params.key ?: STARTING_PAGE_INDEX
        return try {
            val response = apiService.getMovieTrendingDay(currentPage)
            val data = response.body()?.results
            val responseData = mutableListOf<MovieTrendingResult>()
            if (data != null) responseData.addAll(data)
            LoadResult.Page(
                data = responseData,
                prevKey = if (currentPage == STARTING_PAGE_INDEX) null else currentPage - 1,
                nextKey = if (data?.isEmpty() == true) null else currentPage + 1
            )
        } catch (httpEx: HttpException) {
            LoadResult.Error(httpEx)
        } catch (ex: Exception) {
            LoadResult.Error(ex)
        }
    }
}