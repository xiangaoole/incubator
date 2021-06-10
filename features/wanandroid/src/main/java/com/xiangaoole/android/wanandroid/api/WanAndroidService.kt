package com.xiangaoole.android.wanandroid.api

import com.xiangaoole.android.incubator.App
import com.xiangaoole.android.wanandroid.constant.Constant
import com.xiangaoole.android.wanandroid.constant.HttpHelper
import com.xiangaoole.android.wanandroid.http.interceptor.HeaderInterceptor
import com.xiangaoole.android.wanandroid.http.interceptor.SaveCookieInterceptor
import com.xiangaoole.android.wanandroid.model.*
import okhttp3.Cache
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.*
import java.io.File

/**
 * WanAndroid communication setup via Retrofit.
 */
interface WanAndroidService {

    /**
     * 用户登录 http://www.wanandroid.com/user/login
     */
    @POST("user/login")
    @FormUrlEncoded
    suspend fun login(
        @Field("username") username: String,
        @Field("password") password: String
    ): HttpResult<LoginData>

    /**
     * 退出登录 http://www.wanandroid.com/user/logout/json
     *
     * 服务端会让客户端清除 Cookie（即cookie max-Age=0），如果客户
     * 端 Cookie 实现合理，可以实现自动清理，如果本地做了用户账号密码和
     * 保存，及时清理
     */
    @GET("user/logout/json")
    suspend fun logout(): HttpResult<Any>

    /**
     * 收藏的文章列表
     *
     * @param page 页码，拼接在链接中，从 0 开始
     */
    @GET("lg/collect/list/{page}/json")
    suspend fun getCollectList(
        @Path("page") page: Int
    ): HttpResult<BaseListData<CollectionArticle>>

    /**
     * 首页滚动横幅：https://www.wanandroid.com/banner/json
     */
    @GET("banner/json")
    suspend fun getBanners(): HttpResult<List<Banner>>

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
    ): HttpResult<BaseListData<HomeArticle>>

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
    ): HttpResult<BaseListData<Project>>

    @GET("wxarticle/chapters/json")
    suspend fun getWechatArticleTree(): HttpResult<List<WechatBranch>>

    @GET("wxarticle/list/{id}/{page}/json")
    suspend fun getWechatArticles(
        @Path("id") id: Int,
        @Path("page") page: Int
    ): HttpResult<BaseListData<Wechat>>


    companion object {
        const val DEFAULT_START_PAGE_INDEX: Int = 1
        const val OLD_START_PAGE_INDEX: Int = 0

        private const val BASE_URL = "https://www.wanandroid.com"

        fun create(): WanAndroidService {
            return Retrofit.Builder()
                .client(getOkHttpClient())
                .baseUrl(BASE_URL)
                .addConverterFactory(MoshiConverterFactory.create())
                .build()
                .create(WanAndroidService::class.java)
        }

        private fun getOkHttpClient(): OkHttpClient {
            val builder = OkHttpClient.Builder()
            val cacheFile = File(App.context.cacheDir, "cache")
            val cache = Cache(cacheFile, HttpHelper.MAX_CACHE_SIZE)
            return builder.apply {
                addInterceptor(SaveCookieInterceptor())
                addInterceptor(HeaderInterceptor())
                cache(cache)
                retryOnConnectionFailure(true)
            }.build()
        }

    }
}