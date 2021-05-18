package com.xiangaoole.android.module_home

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import timber.log.Timber

class HomeActivity : AppCompatActivity(R.layout.activity_home) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Timber.d("onCreate")
    }
}