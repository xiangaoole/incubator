package com.xiangaoole.android.wanandroid.model

import androidx.room.PrimaryKey
import com.squareup.moshi.Json

data class HttpResult<T>(
    @Json(name = "data") val data: T,
    @Json(name = "errorCode") val errorCode: Int,
    @Json(name = "errorMsg") val errorMsg: String
)

data class ProjectTree( // for Project and WechatArticle
    @Json(name = "courseId") val courseId: Int = -1,
    @Json(name = "id") val id: Int = -1,
    @Json(name = "name") val name: String = "",
    @Json(name = "order") val order: Int = -1,
    @Json(name = "parentChapterId") val parentChapterId: Int = -1,
)

data class ProjectList(
    @Json(name = "currentPage") val currentPage: Int = 1,
    @Json(name = "over") val over: Boolean = false,
    @Json(name = "size") val size: Int = 0,
    @Json(name = "total") val total: Int = 0,
    @Json(name = "datas") val datas: List<Project> = emptyList()
)