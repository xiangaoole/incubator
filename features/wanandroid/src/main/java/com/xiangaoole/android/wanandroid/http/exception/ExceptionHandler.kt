package com.xiangaoole.android.wanandroid.http.exception

import timber.log.Timber
import java.lang.Exception
import java.lang.RuntimeException
import java.net.UnknownHostException

class ExceptionHandler {
    companion object {
        fun handle(e: Throwable): String {
            var errorMsg: String = ""
            Timber.e(e)
            errorMsg = when (e) {
                is RuntimeException -> {
                    e.toString()
                }
                is UnknownHostException ->
                    "网络连接异常"
                is IllegalArgumentException ->
                    "参数错误"
                else ->
                    "未知错误，可能抛锚了吧～"
            }
            return errorMsg
        }
    }
}