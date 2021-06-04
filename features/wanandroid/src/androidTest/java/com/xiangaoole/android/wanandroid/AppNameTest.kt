package com.xiangaoole.android.wanandroid

import android.app.ActivityManager
import android.content.Context
import android.os.Process
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.Test
import org.junit.runner.RunWith
import java.io.File

@RunWith(AndroidJUnit4::class)
class AppNameTest {

    @Test
    fun testProcessName() {
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        println("harold: ${getProcessName(appContext)}")
        println("harold: ${getProcessName2()}")
    }


    private fun getProcessName(context: Context): String? {
        val am = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        for (proInfo in am.runningAppProcesses) {
            if (proInfo.pid == Process.myPid()) {
                return proInfo.processName
            }
        }
        return null
    }

    private fun getProcessName2(): String? {
        try {
            File("/proc/${Process.myPid()}/cmdline")
                .bufferedReader()
                .use {
                    return it.readLine().trim()

                }
        } catch (e: Exception) {
            return null
        }
    }
}