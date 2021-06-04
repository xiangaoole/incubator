package com.xiangaoole.android.wanandroid.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.Json

data class HttpResult<T>(
    @Json(name = "data") val data: T,
    @Json(name = "errorCode") val errorCode: Int,
    @Json(name = "errorMsg") val errorMsg: String
)

data class ProjectTree(
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

@Entity(tableName = "projects")
data class Project(
    @Json(name = "id") @PrimaryKey val id: Long,
    @Json(name = "chapterId") val chapterId: Long, // Project cid
    @Json(name = "author") val author: String,
    @Json(name = "title") val title: String,
    @Json(name = "desc") val desc: String,
    @Json(name = "envelopePic") val envelopePic: String,
    @Json(name = "publishTime") val publishTime: Long, // Date millis
    @Json(name = "niceDate") val niceDate: String?, // Date formatted in Local.CHINA
    @Json(name = "link") val link: String, // https://www.wanandroid.com/blog/show/{link}
)

// Wechat Article
data class WechatTree(
    @Json(name = "courseId") val courseId: Int = -1,
    @Json(name = "id") val id: Int = -1,
    @Json(name = "name") val name: String = "",
    @Json(name = "order") val order: Int = -1,
    @Json(name = "parentChapterId") val parentChapterId: Int = -1,
)

data class WechatList(
    @Json(name = "currentPage") val currentPage: Int = 1,
    @Json(name = "over") val over: Boolean = false,
    @Json(name = "size") val size: Int = 0,
    @Json(name = "total") val total: Int = 0,
    @Json(name = "datas") val datas: List<Wechat> = emptyList()
)

data class Wechat(
    @Json(name = "id") val id: Long,
    @Json(name = "chapterId") val chapterId: Long,
    @Json(name = "author") val author: String,
    @Json(name = "title") val title: String,
    @Json(name = "desc") val desc: String,
    @Json(name = "envelopePic") val envelopePic: String,
    @Json(name = "publishTime") val publishTime: Long, // Date millis
    @Json(name = "niceDate") val niceDate: String?, // Date formatted in Local.CHINA
    @Json(name = "link") val link: String, // https://www.wanandroid.com/blog/show/{link}
)