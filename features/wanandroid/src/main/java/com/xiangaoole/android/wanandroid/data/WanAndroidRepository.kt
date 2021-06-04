package com.xiangaoole.android.wanandroid.data

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.xiangaoole.android.wanandroid.api.WanAndroidService
import com.xiangaoole.android.wanandroid.db.WanAndroidDatabase
import com.xiangaoole.android.wanandroid.model.Project
import com.xiangaoole.android.wanandroid.model.ProjectTree
import com.xiangaoole.android.wanandroid.model.Wechat
import com.xiangaoole.android.wanandroid.model.WechatTree
import com.xiangaoole.android.wanandroid.ui.wechat.WechatListPagingSource
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

    /**
     * Get Project Tree
     */
    suspend fun getProjectTree(): List<ProjectTree> {
        return service.getProjectTree().data
    }

    /**
     * Get Wechat Article Tree
     */
    suspend fun getWechatArticleTree(): List<WechatTree> {
        return service.getWechatArticleTree().data
    }

    fun getWechatArticles(id: Int): Flow<PagingData<Wechat>> {
        Timber.d("getCompleteProjectsStream")

        val pagingSourceFactory = { WechatListPagingSource(service, id) }

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