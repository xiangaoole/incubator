package com.xiangaoole.android.incubator

import android.app.Application
import android.content.Context
import android.view.Choreographer
import com.xiangaoole.android.module_base.BuildConfig
import timber.log.Timber
import java.util.concurrent.TimeUnit
import kotlin.properties.Delegates

class App : Application() {

    companion object {
        var context: Context by Delegates.notNull()
            private set
    }

    override fun onCreate() {
        super.onCreate()
        context = applicationContext
        if (BuildConfig.DEBUG) Timber.plant(Timber.DebugTree())

        listenDoFrame()
    }

    private fun listenDoFrame() {
        Choreographer.getInstance().postFrameCallback(object : Choreographer.FrameCallback {

            var lastFrameTimeNanos: Long = 0

            override fun doFrame(frameTimeNanos: Long) {
                Timber.d("==doFrame==")
                val diffMs: Long = TimeUnit.MILLISECONDS.convert(
                    frameTimeNanos - lastFrameTimeNanos,
                    TimeUnit.NANOSECONDS
                )
                if (lastFrameTimeNanos == 0L) {
                    lastFrameTimeNanos = frameTimeNanos
                    return
                }

                Timber.d("sinceLast: $diffMs")
                if (diffMs > 16.6f) {
                    Timber.e("Lost frame!!!")
                }
            }

        })
    }
}