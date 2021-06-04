package com.xiangaoole.android.incubator

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.xiangaoole.android.meditation.MeditationActivity
import com.xiangaoole.android.module_home.HomeActivity
import com.xiangaoole.android.wanandroid.ui.WanAndroidActivity
import timber.log.Timber
import kotlin.reflect.KClass

class MainActivity : AppCompatActivity(R.layout.activity_main) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Timber.d("MainActivity onCreate")
    }

    fun toHome(view: View) {
        jumpTo(HomeActivity::class)
    }

    fun toMeditation(view: View) {
        jumpTo(MeditationActivity::class)
    }

    fun toWanAndroid(view: View) {
        jumpTo(WanAndroidActivity::class)
    }

    private fun jumpTo(activityClass: KClass<*>) {
        startActivity(Intent(this, activityClass.java))
    }

    fun toTestTask(view: View) {
        jumpTo(LearnTaskActivity::class)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        Timber.d("onSaveInstanceState")
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        Timber.d("onSaveInstanceState")
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        Timber.d("onConfigurationChanged")
    }
}