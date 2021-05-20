package com.xiangaoole.android.wanandroid.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "project_remote_key")
data class ProjectRemoteKey(
    @PrimaryKey val projectId: Long,
    val prevKey: Int?,
    val nextKey: Int?
)