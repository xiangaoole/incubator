package com.xiangaoole.android.wanandroid.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.xiangaoole.android.wanandroid.model.Project

@Database(
    entities = [Project::class, ProjectRemoteKey::class],
    version = 1
)
abstract class WanAndroidDatabase : RoomDatabase() {

    /**
     * Get [Project] table DAO
     */
    abstract fun projectDao(): ProjectDao

    /**
     * Get [ProjectRemoteKey] table DAO
     */
    abstract fun projectRemoteKeyDao(): ProjectRemoteKeyDao

    companion object {
        @Volatile
        private var INSTANCE: WanAndroidDatabase? = null

        fun getInstance(context: Context): WanAndroidDatabase {
            if (INSTANCE == null) {
                synchronized(this) {
                    if (INSTANCE == null) {
                        INSTANCE = Room.databaseBuilder(
                            context.applicationContext,
                            WanAndroidDatabase::class.java,
                            "WanAndroid.db"
                        ).build()
                    }
                }
            }
            return INSTANCE!!
        }
    }
}