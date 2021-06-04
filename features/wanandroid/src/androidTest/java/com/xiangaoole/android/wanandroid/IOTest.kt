package com.xiangaoole.android.wanandroid

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import java.io.*

@RunWith(AndroidJUnit4::class)
class IOTest {

    @Test
    fun test_FileRename() {
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        val testDir = File(appContext.cacheDir, "test")
        assertTrue(ensureDirExist(testDir))
        val name = "IOTest"
        val file = File(testDir, "$name.txt")
        val backupFile = File(file.path + ".bak")
        file.delete()
        backupFile.delete()

        assertTrue(!file.exists())
        assertTrue(!backupFile.exists())

        writeStringToFile("Test content.", file)
        assertTrue(file.exists())

        file.renameTo(backupFile)
        assertTrue(!file.exists())
        assertTrue(backupFile.exists())
    }

    private fun ensureDirExist(dir: File): Boolean {
        if (!dir.exists()) {
            dir.mkdir()
        }
        return dir.exists()
    }

    private fun writeStringToFile(string: String, file: File) {
        BufferedOutputStream(FileOutputStream(file))
            .bufferedWriter().append(string)
    }
}