package com.xiangaoole.android.wanandroid.data

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.xiangaoole.android.wanandroid.api.WanAndroidService
import com.xiangaoole.android.wanandroid.data.paging.BasePagingSource
import com.xiangaoole.android.wanandroid.db.WanAndroidDatabase
import com.xiangaoole.android.wanandroid.model.*
import com.xiangaoole.android.wanandroid.ui.home.HomeArticlesPagingSource
import com.xiangaoole.android.wanandroid.ui.wechat.WechatListPagingSource
import kotlinx.coroutines.flow.Flow
import timber.log.Timber
import kotlin.jvm.Throws

class WanAndroidRepository(
    private val service: WanAndroidService,
    private val database: WanAndroidDatabase
) {

    fun getProjectsStream(cid: Int): Flow<PagingData<Project>> {
        Timber.d("getCompleteProjectsStream")

        //val pagingSourceFactory = { database.projectDao().getProjectsByChapterId(cid) }
        val pagingSourceFactory = {
            BasePagingSource(service, false) { position ->
                this.getProject(position, cid)
            }
        }

        //@OptIn(ExperimentalPagingApi::class)
        return Pager(
            config = PagingConfig(pageSize = DEFAULT_PAGE_SIZE, enablePlaceholders = false),
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
    suspend fun getWechatArticleTree(): List<WechatBranch> {
        return service.getWechatArticleTree().data
    }

    fun getWechatArticles(id: Int): Flow<PagingData<Wechat>> {
        Timber.d("get WechatArticles")

        val pagingSourceFactory = { WechatListPagingSource(service, id) }

        //@OptIn(ExperimentalPagingApi::class)
        return Pager(
            config = PagingConfig(pageSize = DEFAULT_PAGE_SIZE, enablePlaceholders = false),
            //remoteMediator = ProjectMediator(cid, service, database),
            pagingSourceFactory = pagingSourceFactory
        ).flow
    }

    /**
     * Get Home Articles
     */
    fun getHomeArticles(): Flow<PagingData<HomeArticle>> {
        Timber.d("get HomeArticles")

        val pagingSourceFactory = { HomeArticlesPagingSource(service) }

        return Pager(
            config = PagingConfig(pageSize = DEFAULT_PAGE_SIZE, enablePlaceholders = false),
            pagingSourceFactory = pagingSourceFactory
        ).flow
    }

    /**
     * Get Banners
     */
    @Throws(Exception::class)
    suspend fun getBanners(): List<Banner> {
        return service.getBanners().data
    }

    fun getCollectionArticles(): Flow<PagingData<CollectionArticle>> {
        Timber.d("get getCollectionArticles")

        val pagingSourceFactory = {
            BasePagingSource(service, true) { position ->
                this.getCollectList(position)
            }
        }

        return Pager(
            config = PagingConfig(pageSize = DEFAULT_PAGE_SIZE, enablePlaceholders = false),
            pagingSourceFactory = pagingSourceFactory
        ).flow
    }

    suspend fun login(username: String, password: String) =
        service.login(username, password)

    suspend fun logout() = service.logout()

    companion object {
        const val DEFAULT_PAGE_SIZE = 15
    }
}