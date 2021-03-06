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

    @Test
    fun test_getProjectTree() {
        runBlocking {
            val projectTree = service.getProjectTree().data
            for (tree in projectTree) {
                println("Harold: $tree")
            }
        }
    }

    @Test
    fun test_getWechatArticleTree() {
        runBlocking {
            val wechatArticleTree = service.getWechatArticleTree().data
            for (tree in wechatArticleTree) {
                println("Harold: $tree")
            }
        }
    }

    @Test
    fun test_getWechatArticles() = runBlocking {
        val id = 408
        val wechatArticles = service.getWechatArticles(id, 1).data.datas
        for (article in wechatArticles) {
            println("Harold: $article")
        }
    }

}