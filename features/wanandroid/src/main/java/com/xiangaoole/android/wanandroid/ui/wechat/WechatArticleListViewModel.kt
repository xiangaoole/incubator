package com.xiangaoole.android.wanandroid.ui.wechat

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.xiangaoole.android.wanandroid.data.WanAndroidRepository
import com.xiangaoole.android.wanandroid.model.Project
import kotlinx.coroutines.flow.Flow

class WechatArticleListViewModel(
    repository: WanAndroidRepository,
    id: Int
) : ViewModel() {
    val itemList: Flow<PagingData<Project>> = repository.getWechatArticles(id)
        .cachedIn(viewModelScope)

    class Factory(
        private val repository: WanAndroidRepository,
        private val listId: Int
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(WechatArticleListViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return WechatArticleListViewModel(repository, listId) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}