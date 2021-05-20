package com.xiangaoole.android.wanandroid.data

import androidx.paging.*
import androidx.room.withTransaction
import com.xiangaoole.android.wanandroid.api.WanAndroidService
import com.xiangaoole.android.wanandroid.api.WanAndroidService.Companion.PROJECT_START_PAGE_INDEX
import com.xiangaoole.android.wanandroid.db.ProjectRemoteKey
import com.xiangaoole.android.wanandroid.db.WanAndroidDatabase
import com.xiangaoole.android.wanandroid.model.Project
import timber.log.Timber

@OptIn(ExperimentalPagingApi::class)
class ProjectMediator(
    private val cid: Int,
    private val service: WanAndroidService,
    private val database: WanAndroidDatabase
) : RemoteMediator<Int, Project>() {

    override suspend fun initialize(): InitializeAction {
        return InitializeAction.LAUNCH_INITIAL_REFRESH
    }

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, Project>
    ): MediatorResult {

        val position = when (loadType) {
            LoadType.REFRESH -> {
                val remoteKey = getRemoteKeyClosestToCurrentPosition(state)
                remoteKey?.nextKey?.minus(1) ?: PROJECT_START_PAGE_INDEX
            }
            LoadType.APPEND -> {
                val remoteKey = getRemoteKeyForLastItem(state)
                val nextKey = remoteKey?.nextKey
                    ?: return MediatorResult.Success(endOfPaginationReached = remoteKey != null)
                nextKey
            }
            LoadType.PREPEND -> {
                val remoteKey = getRemoteKeyForFirstItem(state)
                val prevKey = remoteKey?.prevKey
                    ?: return MediatorResult.Success(endOfPaginationReached = remoteKey != null)
                prevKey
            }
        }
        return try {
            val response = service.getProject(position, cid)
            if (response.errorCode < 0) {
                Timber.d("errorMsg: ${response.errorMsg}")
                return MediatorResult.Error(Exception(response.errorMsg))
            }

            val endOfPaginationReached = response.data.over
            val projects = response.data.datas

            val nextKey = if (endOfPaginationReached) null else position + 1
            val prevKey =
                if (position == PROJECT_START_PAGE_INDEX) null else position - 1
            val keys = projects.map { ProjectRemoteKey(it.id, prevKey, nextKey) }

            database.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    database.projectDao().clear()
                    database.projectRemoteKeyDao().clear()
                }
                database.projectDao().insertAll(projects)
                database.projectRemoteKeyDao().insertAll(keys)
            }

            MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)
        } catch (exception: Exception) {
            Timber.d("exception: $exception")
            MediatorResult.Error(exception)
        }
    }

    private suspend fun getRemoteKeyClosestToCurrentPosition(state: PagingState<Int, Project>): ProjectRemoteKey? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestItemToPosition(anchorPosition)?.id?.let { projectId ->
                database.projectRemoteKeyDao().getKeyByProjectId(projectId)
            }
        }
    }

    private suspend fun getRemoteKeyForLastItem(state: PagingState<Int, Project>): ProjectRemoteKey? {
        return state.lastItemOrNull()?.id?.let { projectId ->
            database.projectRemoteKeyDao().getKeyByProjectId(projectId)
        }
    }

    private suspend fun getRemoteKeyForFirstItem(state: PagingState<Int, Project>): ProjectRemoteKey? {
        return state.firstItemOrNull()?.id?.let { projectId ->
            database.projectRemoteKeyDao().getKeyByProjectId(projectId)
        }
    }
}