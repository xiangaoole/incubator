package com.xiangaoole.android.wanandroid.http.interceptor

import com.xiangaoole.android.wanandroid.constant.HttpHelper
import com.xiangaoole.android.wanandroid.util.Preference
import okhttp3.Interceptor
import okhttp3.Response
import timber.log.Timber

/**
 * 如果有保存 Cookie，则添加到请求头中
 */
class HeaderInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val builder = request.newBuilder()
        builder.addHeader("Content-type", "application/json; charset=utf-8")
        val host = request.url().host()
        val url = request.url().toString()
        if (host.endsWith(HttpHelper.SAVE_COOKIE_HOST) && (url.contains(HttpHelper.ARTICLE_WEBSITE)
                    || url.contains(HttpHelper.COIN_WEBSITE)
                    || url.contains(HttpHelper.COLLECT_WEBSITE)
                    || url.contains(HttpHelper.UNCOLLECT_WEBSITE))
        ) {
            Timber.d("Harold: read cookie")
            val cookie = Preference.getPref(host, "")
            if (cookie.isNotEmpty()) {
                builder.addHeader(HttpHelper.COOKIE_NAME, cookie)
                Timber.d("Harold: add cookie")
            }
        }
        return chain.proceed(builder.build())
    }
}