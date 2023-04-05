package com.taufik.themovieshow.data.paging.tvshow

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.taufik.themovieshow.data.repository.TheMovieShowRepository
import com.taufik.themovieshow.model.response.tvshow.popularairingtoday.TvShowsMainResult
import com.taufik.themovieshow.utils.CommonConstants
import retrofit2.HttpException

class TvShowsPopularPagingSource(
    private val repository: TheMovieShowRepository
): PagingSource<Int, TvShowsMainResult>() {
    override fun getRefreshKey(state: PagingState<Int, TvShowsMainResult>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, TvShowsMainResult> {
        val currentPage = params.key ?: CommonConstants.STARTING_PAGE_INDEX
        return try {
            val response = repository.getTvShowsPopular(currentPage)
            val data = response.body()?.results
            val responseData = mutableListOf<TvShowsMainResult>()
            if ( data != null) responseData.addAll(data)
            LoadResult.Page(
                data = responseData,
                prevKey = if (currentPage == 1) null else -1,
                nextKey = currentPage.plus(1)
            )
        } catch (httpEx: HttpException) {
            LoadResult.Error(httpEx)
        } catch (ex: Exception) {
            LoadResult.Error(ex)
        }
    }
}