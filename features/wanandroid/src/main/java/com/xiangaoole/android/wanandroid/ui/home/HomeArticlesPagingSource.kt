package com.xiangaoole.android.wanandroid.ui.home

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.xiangaoole.android.wanandroid.api.WanAndroidService
import com.xiangaoole.android.wanandroid.model.HomeArticle
import timber.log.Timber

/**
 * [PagingSource] for [HomeArticle]s.
 */
class HomeArticlesPagingSource(
    private val service: WanAndroidService
) : PagingSource<Int, HomeArticle>() {
    override fun getRefreshKey(state: PagingState<Int, HomeArticle>): Int? {
        // We need to get the previous key (or next key if previous is null) of the page
        // that was closest to the most recently accessed index.
        // Anchor position is the most recently accessed index
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, HomeArticle> {
        val position = params.key ?: WanAndroidService.OLD_START_PAGE_INDEX - 1
        return try {
            // Merge top articles and home articles
            val response: List<HomeArticle> =
                if (position == WanAndroidService.OLD_START_PAGE_INDEX - 1) {
                    service.getTopArticles().data.onEach { homeArticle -> homeArticle.top = true }
                } else {
                    service.getHomeArticles(position).data.datas
                }
            val nextKey = if (response.isEmpty()) {
                null
            } else {
                position + 1
            }
            Timber.d("position: $position, loadSize: ${params.loadSize}, nextKey: $nextKey")
            LoadResult.Page(
                data = response,
                prevKey = if (position == WanAndroidService.OLD_START_PAGE_INDEX - 1) null else position - 1,
                nextKey = nextKey
            )
        } catch (exception: Exception) {
            Timber.d("exception: $exception")
            LoadResult.Error(exception)
        }
    }
}