package com.xiangaoole.android.wanandroid.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.xiangaoole.android.wanandroid.data.WanAndroidRepository
import com.xiangaoole.android.wanandroid.model.Project
import kotlinx.coroutines.flow.Flow

class ProjectListViewModel(repository: WanAndroidRepository, cid: Int) : ViewModel() {

    val projectList: Flow<PagingData<Project>> = repository.getProjectsStream(cid)
        .cachedIn(viewModelScope)

    class ProjectListViewModelFactory(
        private val repository: WanAndroidRepository,
        private val cid: Int
    ) :
        ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(ProjectListViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return ProjectListViewModel(repository, cid) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}