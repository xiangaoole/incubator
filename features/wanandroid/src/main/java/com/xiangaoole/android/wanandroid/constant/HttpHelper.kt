package com.xiangaoole.android.wanandroid.constant

import androidx.core.content.edit
import com.xiangaoole.android.wanandroid.util.Preference
import java.lang.StringBuilder

object HttpHelper {
    const val SAVE_COOKIE_HOST = "wanandroid.com"
    const val SAVE_USER_LOGIN_KEY = "user/login"
    const val SAVE_USER_REGISTER_KEY = "user/register"

    const val SET_COOKIE_KEY = "set-cookie"
    const val COOKIE_NAME = "Cookie"

    const val COLLECT_WEBSITE = "lg/collect"
    const val UNCOLLECT_WEBSITE = "lg/uncollect"
    const val ARTICLE_WEBSITE = "article"
    const val COIN_WEBSITE = "lg/coin"

    /**
     * Cache size for OkHttp3
     */
    const val MAX_CACHE_SIZE: Long = 1024 * 1024 * 10

    fun encodeCookie(cookies: List<String>): String {
        val sb = StringBuilder()
        val set = HashSet<String>()
        cookies.flatMap { cookie -> cookie.split(";".toRegex()).dropLastWhile { it.isEmpty() } }
            .forEach { set.add(it.trim()) }
        for (cookie in set) {
            sb.append(cookie).append(";")
        }
        if (sb.endsWith(";")) {
            sb.deleteCharAt(sb.length - 1)
        }
        return sb.toString()
    }

    fun saveCookie(host: String?, cookies: List<String>) {
        host ?: return
        Preference.getPrefs().edit {
            putString(host, encodeCookie(cookies))
        }
    }
}