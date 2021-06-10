package com.xiangaoole.android.wanandroid.util

import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import androidx.appcompat.app.AlertDialog
import com.xiangaoole.android.wanandroid.R

/**
 * 获取 Dialog 的工具类
 */
object DialogUtil {

    fun getDialog(context: Context): AlertDialog.Builder {
        return AlertDialog.Builder(context)
    }

    fun getConfirmDialog(
        context: Context, message: String,
        onClickListener: DialogInterface.OnClickListener
    ): AlertDialog.Builder {
        return getDialog(context).apply {
            setMessage(message)
            setNegativeButton(android.R.string.cancel, null)
            setPositiveButton(android.R.string.ok, onClickListener)
        }
    }
}