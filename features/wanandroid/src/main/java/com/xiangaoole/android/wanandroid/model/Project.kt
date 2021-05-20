package com.xiangaoole.android.wanandroid.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.Json

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