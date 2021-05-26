package com.xiangaoole.android.wanandroid.data

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.xiangaoole.android.wanandroid.api.WanAndroidService
import com.xiangaoole.android.wanandroid.api.WanAndroidService.Companion.COMPLETE_PROJECT_CID
import com.xiangaoole.android.wanandroid.db.WanAndroidDatabase
import com.xiangaoole.android.wanandroid.model.Project
import kotlinx.coroutines.flow.Flow
import timber.log.Timber

class WanAndroidRepository(
    private val service: WanAndroidService,
    private val database: WanAndroidDatabase
) {

    fun getProjectsStream(cid: Int): Flow<PagingData<Project>> {
        Timber.d("getCompleteProjectsStream")

        //val pagingSourceFactory = { database.projectDao().getProjectsByChapterId(cid) }
        val pagingSourceFactory = { ProjectPagingSource(service, cid) }

        //@OptIn(ExperimentalPagingApi::class)
        return Pager(
            config = PagingConfig(pageSize = PROJECT_PAGE_SIZE, enablePlaceholders = false),
            //remoteMediator = ProjectMediator(cid, service, database),
            pagingSourceFactory = pagingSourceFactory
        ).flow
    }

    companion object {
        const val PROJECT_PAGE_SIZE = 15
    }
}