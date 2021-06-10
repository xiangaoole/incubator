package com.xiangaoole.android.wanandroid.http.interceptor

import com.xiangaoole.android.wanandroid.constant.HttpHelper
import okhttp3.Interceptor
import okhttp3.Response
import timber.log.Timber

/**
 * 保存 Cookie 到 SharedPreferences
 */
class SaveCookieInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val response = chain.proceed(request)
        val requestUrl = request.url().toString()
        val host = request.url().host()

        if (host.endsWith(HttpHelper.SAVE_COOKIE_HOST)
            && (requestUrl.contains(HttpHelper.SAVE_USER_LOGIN_KEY)
                    || requestUrl.contains(HttpHelper.SAVE_USER_REGISTER_KEY))
            && response.headers(HttpHelper.SET_COOKIE_KEY).isNotEmpty()
        ) {
            val cookies = response.headers(HttpHelper.SET_COOKIE_KEY)
            Timber.d("Harold: $cookies")
            Timber.d("Harold: $host")
            HttpHelper.saveCookie(host, cookies)
        }
        return response
    }
}