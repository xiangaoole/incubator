package com.xiangaoole.android.wanandroid.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.xiangaoole.android.wanandroid.data.WanAndroidRepository
import com.xiangaoole.android.wanandroid.model.CollectionArticle
import kotlinx.coroutines.flow.Flow

class CollectionViewModel(
    repository: WanAndroidRepository
) : ViewModel() {

    val collectionList: Flow<PagingData<CollectionArticle>> =
        repository.getCollectionArticles().cachedIn(viewModelScope)

    class Factory(
        private val repository: WanAndroidRepository,
    ) :
        ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(CollectionViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return CollectionViewModel(repository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}