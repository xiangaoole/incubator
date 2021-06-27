package com.xiangaoole.android.wanandroid.http.exception

/**
 * errorCode如果为负数则认为错误，此时errorMsg会包含错误信息
 */
object ErrorCode {
    /**
     * errorCode = 0 代表执行成功，不建议依赖任何非0的 errorCode
     */
    const val SUCCESS: Int = 0

    /**
     * errorCode = -1001 代表登录失效，需要重新登录。
     */
    const val NEED_LOGIN: Int = -1001
}