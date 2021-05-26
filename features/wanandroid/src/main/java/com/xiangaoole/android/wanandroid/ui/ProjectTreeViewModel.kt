package com.xiangaoole.android.wanandroid.ui

import androidx.lifecycle.*
import com.xiangaoole.android.wanandroid.data.WanAndroidRepository
import com.xiangaoole.android.wanandroid.model.ProjectTree
import kotlinx.coroutines.launch

class ProjectTreeViewModel(repository: WanAndroidRepository) : ViewModel() {
    private val _projectTrees = MutableLiveData<List<ProjectTree>>()
    val projectTrees: LiveData<List<ProjectTree>> get() = _projectTrees

    init {
        viewModelScope.launch {
            _projectTrees.value = repository.getProjectTree()
        }
    }

    class ProjectTreeViewModelFactory(
        private val repository: WanAndroidRepository
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(ProjectTreeViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return ProjectTreeViewModel(repository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }

    }
}