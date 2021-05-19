package com.xiangaoole.android.wanandroid.model

import com.squareup.moshi.Json

data class Project(
    @Json(name = "id") val id: Long,
    @Json(name = "author") val author: String?,
    @Json(name = "title") val title: String?,
    @Json(name = "desc") val desc: String?,
    @Json(name = "envelopePic") val envelopePic: String,
    @Json(name = "shareUser") val shareUser: String?,
    @Json(name = "shareDate") val shareDate: Long?, // Date millis
    @Json(name = "niceDate") val niceDate: String?, // Date formatted in Local.CHINA
    @Json(name = "link") val link: String?, // https://www.wanandroid.com/blog/show/{link}
)