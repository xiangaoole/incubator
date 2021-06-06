package com.xiangaoole.android.wanandroid.ui.home

import androidx.lifecycle.*
import androidx.paging.*
import com.xiangaoole.android.wanandroid.data.WanAndroidRepository
import com.xiangaoole.android.wanandroid.model.Banner
import com.xiangaoole.android.wanandroid.model.HomeArticle
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import timber.log.Timber
import kotlin.Exception

class HomeViewModel(
    repository: WanAndroidRepository,
) : ViewModel() {
    private val bannersFlow: Flow<List<Banner>?> = flow {
        val data = try {
            repository.getBanners()
        } catch (e: Exception) {
            Timber.e(e)
            null
        }
        emit(data)
    }

    private var banners: List<Banner>? = null

    val itemList: Flow<PagingData<ListItemModel>> =
        bannersFlow.combine(repository.getHomeArticles()) { banners, article ->
            this.banners = banners // save??? better way?
            article
        }
            .map { pagingData -> pagingData.map { ListItemModel.Item(it) } }
            .map {
                it.insertSeparators { before, _ ->
                    if (before == null) { // only insert header
                        ListItemModel.Header(banners)
                    } else {
                        null
                    }
                }
            }
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

/**
 * ListView Header and Item
 */
sealed class ListItemModel {
    data class Header(val banners: List<Banner>?) : ListItemModel()
    data class Item(val data: HomeArticle) : ListItemModel()
    //class Footer: ListItemModel()
}