package com.xiangaoole.android.incubator

import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class LearnLoadBitmap {

    @Before
    fun init() {
    }

    @After
    fun clear() {
    }

    @Test
    fun loadBitmap() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        // 768*1024: 23,789,568
        // 192*256: 1,486,848
        decodeSampleBitmap(context.resources, R.drawable.big_img, 100, 100)
    }

    private fun decodeSampleBitmap(
        res: Resources,
        resId: Int,
        reqWidth: Int,
        reqHeight: Int
    ) {
        val options = BitmapFactory.Options().apply {
            inPreferredConfig = Bitmap.Config.RGB_565
            val origin = BitmapFactory.decodeResource(res, resId, this)
            println("Harold: $outWidth*$outHeight: ${origin.byteCount}")
            var size = 1
            if (outHeight > reqHeight || outWidth > reqWidth) {
                val halfH = outHeight / 2
                val halfW = outWidth / 2
                while (halfH / size >= reqHeight
                    && halfW / size >= reqWidth
                ) {
                    size *= 2
                }
            }
            inSampleSize = size

            val decoded = BitmapFactory.decodeResource(res, resId, this)
            println("Harold: $outWidth*$outHeight: ${decoded.byteCount}")
        }
    }

}