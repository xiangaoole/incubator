package com.xiangaoole.android.wanandroid.db

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.xiangaoole.android.wanandroid.model.Project

@Dao
interface ProjectDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(projects: List<Project>)

    @Query(
        "SELECT * FROM projects WHERE chapterId=:chapterId ORDER BY publishTime DESC"
    )
    fun getProjectsByChapterId(chapterId: Int): PagingSource<Int, Project>

    @Query("DELETE FROM projects ")
    suspend fun clear()
}