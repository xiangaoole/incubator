package com.xiangaoole.android.wanandroid.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface ProjectRemoteKeyDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(remoteKeys: List<ProjectRemoteKey>)

    @Query("SELECT * FROM project_remote_key WHERE projectId=:id")
    suspend fun getKeyByProjectId(id: Long): ProjectRemoteKey?

    @Query("DELETE FROM project_remote_key ")
    suspend fun clear()
}