package com.xiangaoole.android.wanandroid

import com.xiangaoole.android.wanandroid.api.WanAndroidService
import com.xiangaoole.android.wanandroid.data.WanAndroidRepository
import com.xiangaoole.android.wanandroid.ui.ProjectListViewModelFactory

/**
 * Class that handles object creation.
 * Like this, objects can be passed as parameters in the constructors and then replaced for
 * testing, where needed.
 */
object Injection {
    /**
     * Creates an instance of [WanAndroidRepository] based on the [WanAndroidService]
     */
    private fun provideWanAndroidRepository(): WanAndroidRepository {
        return WanAndroidRepository(WanAndroidService.create())
    }

    /**
     * Provides the [ProjectListViewModelFactory]
     */
    fun provideProjectListVIewModelFactory(): ProjectListViewModelFactory {
        return ProjectListViewModelFactory(provideWanAndroidRepository())
    }
}