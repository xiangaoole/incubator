package com.xiangaoole.android.incubator

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide
import com.xiangaoole.android.incubator.databinding.ActivityLearnTaskBinding

class LearnTaskActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityLearnTaskBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Glide.with(this)
            .load(R.drawable.demo_wanandroid)
            .into(binding.ivThumbnail)
    }
}