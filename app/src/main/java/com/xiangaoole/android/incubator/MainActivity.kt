package com.xiangaoole.android.incubator

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.xiangaoole.android.meditation.MeditationActivity
import com.xiangaoole.android.module_home.HomeActivity
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

    private fun jumpTo(activityClass: KClass<*>) {
        startActivity(Intent(this, activityClass.java))
    }
}