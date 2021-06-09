package com.xiangaoole.android.wanandroid.ext

import android.content.Context
import com.xiangaoole.android.wanandroid.util.Preference
import com.xiangaoole.android.wanandroid.widget.CustomToast

fun Context.showToast(message: String) {
    CustomToast(applicationContext, message).show()
}

fun <T> Context.getPref(name: String, default: T): T =
    with(Preference.getPrefs(applicationContext)) {
        val result = when (default) {
            is Long -> getLong(name, default)
            is String -> getString(name, default)
            is Int -> getInt(name, default)
            is Boolean -> getBoolean(name, default)
            is Float -> getFloat(name, default)
            else -> throw IllegalArgumentException("${default!!::class.qualifiedName}")
        }
        @Suppress("UNCHECKED_CAST")
        result as T
    }