package com.xiangaoole.android.wanandroid.widget

import android.content.Context
import android.view.Gravity
import android.view.View
import android.widget.TextView
import android.widget.Toast
import com.xiangaoole.android.wanandroid.R

/**
 * 自定义的居中显示的 Toast，需要调用 [show] 来显示
 */
class CustomToast {

    private val toast: Toast

    constructor(context: Context, message: String, duration: Int = Toast.LENGTH_SHORT) {
        toast = Toast(context)
        toast.duration = duration
        val customView = View.inflate(context, R.layout.toast_custom, null)
        customView.findViewById<TextView>(R.id.tv_prompt).text = message
        toast.view = customView
        toast.setGravity(Gravity.CENTER, 0, 0)
    }

    fun show() {
        toast.show()
    }
}