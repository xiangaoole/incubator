package com.xiangaoole.android.wanandroid.api

import com.squareup.moshi.Json
import com.xiangaoole.android.wanandroid.model.Project

data class ProjectListResponse(
    @Json(name = "data") val data: ProjectList,
    @Json(name = "errorCode") val errorCode: Int,
    @Json(name = "errorMsg") val errorMsg: String
)

data class ProjectList(
    @Json(name = "currentPage") val currentPage: Int = 1,
    @Json(name = "over") val over: Boolean = false,
    @Json(name = "size") val size: Int = 0,
    @Json(name = "total") val total: Int = 0,
    @Json(name = "datas") val datas: List<Project> = emptyList()
)