package com.xiangaoole.android.wanandroid.util

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import com.xiangaoole.android.incubator.App
import com.xiangaoole.android.wanandroid.constant.Constant
import com.xiangaoole.android.wanandroid.constant.HttpHelper
import com.xiangaoole.android.wanandroid.model.LoginData

class Preference {

    companion object {
        private const val FILE_NAME = "wan_android_pref"

        @Volatile
        private lateinit var sharedPreferences: SharedPreferences

        fun getPrefs(): SharedPreferences {
            if (!::sharedPreferences.isInitialized) {
                synchronized(this) {
                    if (!::sharedPreferences.isInitialized) {
                        sharedPreferences = App.context.getSharedPreferences(
                            FILE_NAME,
                            Context.MODE_PRIVATE
                        )
                    }
                }
            }
            return sharedPreferences
        }

        fun <T> getPref(name: String, default: T): T =
            with(Preference.getPrefs()) {
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

        fun saveLoginData(data: LoginData?) {
            getPrefs().edit {
                if (data == null) {
                    putBoolean(Constant.LOGIN_KEY, false)
                    putString(Constant.USERNAME_KEY, "")
                    putString(Constant.PASSWORD_KEY, "")
                    putString(Constant.TOKEN_KEY, "")
                    putString(HttpHelper.SAVE_COOKIE_HOST, "") // clear cookie
                } else {
                    putBoolean(Constant.LOGIN_KEY, true)
                    putString(Constant.USERNAME_KEY, data.username)
                    putString(Constant.PASSWORD_KEY, data.username)
                    putString(Constant.TOKEN_KEY, data.token)
                }
            }
        }

    }


}