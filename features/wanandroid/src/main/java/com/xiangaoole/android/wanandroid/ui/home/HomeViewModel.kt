package com.xiangaoole.android.wanandroid.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.xiangaoole.android.wanandroid.data.WanAndroidRepository
import com.xiangaoole.android.wanandroid.model.HomeArticle
import kotlinx.coroutines.flow.Flow

class HomeViewModel(
    repository: WanAndroidRepository,
) : ViewModel() {
    val itemList: Flow<PagingData<HomeArticle>> = repository.getHomeArticles()
        .cachedIn(viewModelScope)

    class Factory(
        private val repository: WanAndroidRepository,
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return HomeViewModel(repository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}