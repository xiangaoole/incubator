package com.xiangaoole.android.wanandroid.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.xiangaoole.android.wanandroid.data.WanAndroidRepository

class ProjectListViewModelFactory(private val repository: WanAndroidRepository) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ProjectListViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ProjectListViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}