package com.xiangaoole.android.wanandroid.ui.wechat

import androidx.lifecycle.*
import com.xiangaoole.android.wanandroid.data.WanAndroidRepository
import com.xiangaoole.android.wanandroid.model.ProjectTree
import kotlinx.coroutines.launch
import timber.log.Timber

class WechatArticleTreeViewModel(
    repository: WanAndroidRepository,
) : ViewModel() {
    private val _dataList = MutableLiveData<List<ProjectTree>>()
    val dataList: LiveData<List<ProjectTree>> get() = _dataList

    init {
        viewModelScope.launch {
            try {
                _dataList.value = repository.getWechatArticleTree()
            } catch (exception: Exception) {
                Timber.e(exception)
            }
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