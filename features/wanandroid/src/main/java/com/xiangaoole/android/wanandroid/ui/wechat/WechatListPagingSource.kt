package com.xiangaoole.android.wanandroid.ui.wechat

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.xiangaoole.android.wanandroid.api.WanAndroidService
import com.xiangaoole.android.wanandroid.api.WanAndroidService.Companion.WECHAT_ARTICLE_START_PAGE_INDEX
import com.xiangaoole.android.wanandroid.model.Wechat
import timber.log.Timber

/**
 * [PagingSource] for Wechat Articles
 */
class WechatListPagingSource(
    private val service: WanAndroidService,
    private val id: Int
) : PagingSource<Int, Wechat>() {
    override fun getRefreshKey(state: PagingState<Int, Wechat>): Int? {
        // We need to get the previous key (or next key if previous is null) of the page
        // that was closest to the most recently accessed index.
        // Anchor position is the most recently accessed index
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Wechat> {
        val position = params.key ?: WECHAT_ARTICLE_START_PAGE_INDEX
        return try {
            val response = service.getWechatArticles(id, position)
            if (response.errorCode < 0) {
                Timber.d("errorMsg: ${response.errorMsg}")
                return LoadResult.Error(Exception(response.errorMsg))
            }
            val nextKey = if (response.data.over) {
                null
            } else {
                position + 1
            }
            Timber.d("position: $position, loadSize: ${params.loadSize}, nextKey: $nextKey")
            val projects = response.data.datas
            LoadResult.Page(
                data = projects,
                prevKey = if (position == WECHAT_ARTICLE_START_PAGE_INDEX) null else position - 1,
                nextKey = nextKey
            )
        } catch (exception: Exception) {
            Timber.d("exception: $exception")
            LoadResult.Error(exception)
        }
    }
}