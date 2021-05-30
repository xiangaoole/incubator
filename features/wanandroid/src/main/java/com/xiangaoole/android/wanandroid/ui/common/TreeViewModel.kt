package com.xiangaoole.android.wanandroid.ui.common

import androidx.lifecycle.*
import com.xiangaoole.android.wanandroid.data.WanAndroidRepository
import com.xiangaoole.android.wanandroid.model.ProjectTree
import kotlinx.coroutines.launch

class WechatArticleTreeViewModel(
    repository: WanAndroidRepository
) : ViewModel() {
    private val _dataList = MutableLiveData<List<ProjectTree>>()
    val dataList: LiveData<List<ProjectTree>> get() = _dataList

    init {
        viewModelScope.launch {
            _dataList.value = repository.getWechatArticleTree()
        }
    }

    class ViewModelFactory(
        private val repository: WanAndroidRepository,
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(WechatArticleTreeViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return WechatArticleTreeViewModel(repository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }

    }
}