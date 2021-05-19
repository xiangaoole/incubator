package com.xiangaoole.android.wanandroid.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.xiangaoole.android.wanandroid.data.WanAndroidRepository
import com.xiangaoole.android.wanandroid.model.Project
import kotlinx.coroutines.flow.Flow

class ProjectListViewModel(repository: WanAndroidRepository) : ViewModel() {

    val projectList: Flow<PagingData<Project>> = repository.getCompleteProjectsStream()
        .cachedIn(viewModelScope)

}