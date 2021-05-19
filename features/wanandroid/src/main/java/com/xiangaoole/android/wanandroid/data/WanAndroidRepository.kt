package com.xiangaoole.android.wanandroid.data

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.xiangaoole.android.wanandroid.api.WanAndroidService
import com.xiangaoole.android.wanandroid.api.WanAndroidService.Companion.COMPLETE_PROJECT_CID
import com.xiangaoole.android.wanandroid.model.Project
import kotlinx.coroutines.flow.Flow
import timber.log.Timber

class WanAndroidRepository(private val service: WanAndroidService) {

    fun getCompleteProjectsStream(): Flow<PagingData<Project>> {
        Timber.d("getCompleteProjectsStream")
        return Pager(
            config = PagingConfig(pageSize = PROJECT_PAGE_SIZE, enablePlaceholders = false),
            pagingSourceFactory = { ProjectPagingSource(service, COMPLETE_PROJECT_CID) }
        ).flow
    }

    companion object {
        const val PROJECT_PAGE_SIZE = 15
    }
}