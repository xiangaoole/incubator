package com.xiangaoole.android.wanandroid.api

import androidx.test.ext.junit.runners.AndroidJUnit4
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import timber.log.Timber

@RunWith(AndroidJUnit4::class)
class WanAndroidServiceTest {

    private lateinit var service: WanAndroidService

    @Before
    fun onCreate() {
        service = WanAndroidService.create()
    }

    @After
    fun onClear() {
    }

    @Test
    fun test_getProject() {
        val cid = 294
        runBlocking {
            val projects = service.getProject(1, cid).data.datas
            for (project in projects) {
                println("Harold: " + project.title)
            }
        }
    }
}