package com.xiangaoole.android.wanandroid

import android.content.Context
import com.xiangaoole.android.wanandroid.api.WanAndroidService
import com.xiangaoole.android.wanandroid.data.WanAndroidRepository
import com.xiangaoole.android.wanandroid.db.WanAndroidDatabase
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
    private fun provideWanAndroidRepository(context: Context): WanAndroidRepository {
        return WanAndroidRepository(
            WanAndroidService.create(),
            WanAndroidDatabase.getInstance(context)
        )
    }

    /**
     * Provides the [ProjectListViewModelFactory]
     */
    fun provideProjectListViewModelFactory(context: Context): ProjectListViewModelFactory {
        return ProjectListViewModelFactory(provideWanAndroidRepository(context))
    }
}