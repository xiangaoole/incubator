package com.xiangaoole.android.wanandroid.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.xiangaoole.android.wanandroid.api.WanAndroidService
import com.xiangaoole.android.wanandroid.api.WanAndroidService.Companion.DEFAULT_START_PAGE_INDEX
import com.xiangaoole.android.wanandroid.api.WanAndroidService.Companion.OLD_START_PAGE_INDEX
import com.xiangaoole.android.wanandroid.http.exception.ExceptionHandler
import com.xiangaoole.android.wanandroid.model.BaseListData
import com.xiangaoole.android.wanandroid.model.HttpResult
import timber.log.Timber

/**
 * @param oldStartIndex start from 0 when true, else 1
 */
class BasePagingSource<T : Any>(
    private val service: WanAndroidService,
    oldStartIndex: Boolean,
    private val getData: suspend WanAndroidService.(position: Int) -> HttpResult<BaseListData<T>>
) :
    PagingSource<Int, T>() {

    private val mStartIndex =
        if (oldStartIndex) OLD_START_PAGE_INDEX
        else DEFAULT_START_PAGE_INDEX

    override fun getRefreshKey(state: PagingState<Int, T>): Int? {
        // We need to get the previous key (or next key if previous is null) of the page
        // that was closest to the most recently accessed index.
        // Anchor position is the most recently accessed index
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, T> {
        val position = params.key ?: mStartIndex
        return try {
            val response = service.getData(position)
            if (response.errorCode < 0) {
                Timber.d("errorMsg: ${response.errorMsg}")
                return LoadResult.Error(Exception(response.errorMsg))
            }
            val nextKey = if (response.data.over) {
                null
            } else {
                position + 1
            }
            val projects = response.data.datas
            LoadResult.Page(
                data = projects,
                prevKey = if (position == mStartIndex) null else position - 1,
                nextKey = nextKey
            )
        } catch (exception: Exception) {
            Timber.d("exception: $exception")
            val humanReadException = Exception(ExceptionHandler.handle(exception))
            LoadResult.Error(humanReadException)
        }
    }
}