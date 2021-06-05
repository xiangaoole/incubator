package com.xiangaoole.android.incubator

import android.app.Application
import android.view.Choreographer
import timber.log.Timber
import java.util.concurrent.TimeUnit

class App : Application() {

    override fun onCreate() {
        super.onCreate()
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