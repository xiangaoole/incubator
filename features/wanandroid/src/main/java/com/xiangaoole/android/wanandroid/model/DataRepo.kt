package com.xiangaoole.android.wanandroid.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.Json

data class HttpResult<T>(
    @Json(name = "data") val data: T,
    @Json(name = "errorCode") val errorCode: Int,
    @Json(name = "errorMsg") val errorMsg: String
)

data class BaseListData<T>(
    @Json(name = "currentPage") val currentPage: Int = 1,
    @Json(name = "offset") val offset: Int,
    @Json(name = "over") val over: Boolean = false,
    @Json(name = "size") val size: Int = 0,
    @Json(name = "pageCount") val pageCount: Int = 0,
    @Json(name = "total") val total: Int = 0,
    @Json(name = "datas") val datas: List<T>
)

// Tag
data class Tag(
    @Json(name = "name") val name: String,
    @Json(name = "url") val url: String
)

// 首页横幅
data class Banner(
    @Json(name = "desc") val desc: String,
    @Json(name = "id") val id: Int,
    @Json(name = "imagePath") val imagePath: String,
    @Json(name = "isVisible") val isVisible: Int,
    @Json(name = "order") val order: Int,
    @Json(name = "title") val title: String,
    @Json(name = "type") val type: Int,
    @Json(name = "url") val url: String
)

// 首页文章列表
data class HomeArticle(
    @Json(name = "author") val author: String = "",
    @Json(name = "chapterId") val chapterId: Long = 0,
    @Json(name = "chapterName") val chapterName: String = "",
    @Json(name = "desc") val desc: String = "",
    @Json(name = "envelopePic") val envelopePic: String = "",
    @Json(name = "fresh") val fresh: Boolean = false,
    @Json(name = "id") val id: Long = 0,
    @Json(name = "link") val link: String,
    @Json(name = "publishTime") val publishTime: Long = 0, // Date millis
    @Json(name = "niceDate") val niceDate: String?, // Date formatted in Local.CHINA
    @Json(name = "shareUser") val shareUser: String?,
    @Json(name = "superChapterName") val superChapterName: String = "",
    @Json(name = "tags") val tags: List<Tag> = emptyList(),
    @Json(name = "title") val title: String = "",
    var top: Boolean = false
)

// 项目
data class ProjectTree(
    @Json(name = "courseId") val courseId: Int = -1,
    @Json(name = "id") val id: Int = -1,
    @Json(name = "name") val name: String = "",
    @Json(name = "order") val order: Int = -1,
    @Json(name = "parentChapterId") val parentChapterId: Int = -1,
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

// 微信公众号
data class WechatBranch(
    @Json(name = "courseId") val courseId: Int = -1,
    @Json(name = "id") val id: Int = -1,
    @Json(name = "name") val name: String = "",
    @Json(name = "order") val order: Int = -1,
    @Json(name = "parentChapterId") val parentChapterId: Int = -1,
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

// 收藏文章
// 除了文字标题，链接，其他字段都可能为null，一定要注意布局下发 null 时的显示情况。
data class CollectionArticle(
    @Json(name = "author") val author: String = "",
    @Json(name = "chapterId") val chapterId: Long,
    @Json(name = "chapterName") val chapterName: String = "",
    @Json(name = "desc") val desc: String = "",
    @Json(name = "envelopePic") val envelopePic: String = "",
    @Json(name = "fresh") val fresh: Boolean = false,
    @Json(name = "id") val id: Long = 0,
    @Json(name = "link") val link: String,
    @Json(name = "niceDate") val niceDate: String = "", // Date formatted in Local.CHINA
    @Json(name = "publishTime") val publishTime: Long = 0, // Date millis
    @Json(name = "shareUser") val shareUser: String = "",
    @Json(name = "superChapterName") val superChapterName: String = "",
    @Json(name = "tags") val tags: List<Tag> = emptyList(),
    @Json(name = "title") val title: String,
    var top: Boolean = false
)

// 登录信息
data class LoginData(
    @Json(name = "collectIds") val collectIds: List<String>,
    @Json(name = "email") val email: String,
    @Json(name = "icon") val icon: String,
    @Json(name = "id") val id: Int,
    @Json(name = "password") val password: String,
    @Json(name = "token") val token: String,
    @Json(name = "type") val type: Int,
    @Json(name = "username") val username: String
)

// 用户积分信息
data class UserInfoBody(
    @Json(name = "coinCount") val coinCount: Int = -1, // 总积分
    @Json(name = "rank") val rank: Int = -1, // 当前排名
    @Json(name = "userId") val userId: Int = -1,
    @Json(name = "username") val username: String = ""
)
