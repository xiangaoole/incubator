package com.xiangaoole.android.wanandroid.api

import com.xiangaoole.android.wanandroid.model.*
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * WanAndroid communication setup via Retrofit.
 */
interface WanAndroidService {

    /**
     * 置顶文章：https://www.wanandroid.com/article/top/json
     */
    @GET("article/top/json")
    suspend fun getTopArticles(): HttpResult<List<HomeArticle>>

    /**
     * 首页文章列表：https://www.wanandroid.com/article/list/0/json
     */
    @GET("article/list/{page}/json")
    suspend fun getHomeArticles(
        @Path("page") page: Int,
    ): HttpResult<HomeArticleList>

    /**
     * 项目分类 https://www.wanandroid.com/project/tree/json
     */
    @GET("project/tree/json")
    suspend fun getProjectTree(): HttpResult<List<ProjectTree>>

    /**
     * 项目列表
     * https://www.wanandroid.com/project/list/1/json?cid=294
     *
     * @param page  页码：拼接在链接中，从1开始。
     * @param cid   项目分类接口
     */
    @GET("project/list/{page}/json")
    suspend fun getProject(
        @Path("page") page: Int,
        @Query("cid") cid: Int
    ): HttpResult<ProjectList>

    @GET("wxarticle/chapters/json")
    suspend fun getWechatArticleTree(): HttpResult<List<WechatTree>>

    @GET("wxarticle/list/{id}/{page}/json")
    suspend fun getWechatArticles(
        @Path("id") id: Int,
        @Path("page") page: Int
    ): HttpResult<WechatList>


    companion object {
        const val DEFAULT_START_PAGE_INDEX: Int = 1
        const val OLD_START_PAGE_INDEX: Int = 0

        private const val BASE_URL = "https://www.wanandroid.com"

        fun create(): WanAndroidService {
            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(MoshiConverterFactory.create())
                .build()
                .create(WanAndroidService::class.java)
        }
    }
}