package com.xiangaoole.android.wanandroid.ui.wechat

import androidx.lifecycle.*
import com.xiangaoole.android.wanandroid.data.WanAndroidRepository
import com.xiangaoole.android.wanandroid.model.WechatTree
import kotlinx.coroutines.launch
import timber.log.Timber

class WechatTreeViewModel(
    repository: WanAndroidRepository,
) : ViewModel() {
    private val _dataList = MutableLiveData<List<WechatTree>>()
    val dataList: LiveData<List<WechatTree>> get() = _dataList

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
            if (modelClass.isAssignableFrom(WechatTreeViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return WechatTreeViewModel(repository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }

    }
}