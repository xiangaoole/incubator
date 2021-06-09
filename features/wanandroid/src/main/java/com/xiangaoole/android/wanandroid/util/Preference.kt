package com.xiangaoole.android.wanandroid.util

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import com.xiangaoole.android.wanandroid.Constant
import com.xiangaoole.android.wanandroid.model.LoginData

class Preference {

    companion object {
        private const val FILE_NAME = "wan_android_pref"

        @Volatile
        private lateinit var sharedPreferences: SharedPreferences

        fun getPrefs(context: Context): SharedPreferences {
            if (!::sharedPreferences.isInitialized) {
                synchronized(this) {
                    if (!::sharedPreferences.isInitialized) {
                        sharedPreferences = context.applicationContext.getSharedPreferences(
                            FILE_NAME,
                            Context.MODE_PRIVATE
                        )
                    }
                }
            }
            return sharedPreferences
        }

        fun saveLoginData(application: Application, data: LoginData?) {
            getPrefs(application).edit {
                if (data == null) {
                    putBoolean(Constant.LOGIN_KEY, false)
                    putString(Constant.USERNAME_KEY, "")
                    putString(Constant.PASSWORD_KEY, "")
                    putString(Constant.TOKEN_KEY, "")
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