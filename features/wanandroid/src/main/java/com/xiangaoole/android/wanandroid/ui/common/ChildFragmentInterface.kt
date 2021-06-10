package com.xiangaoole.android.wanandroid.ui.common

import android.view.View

/**
 * 传递 scrollToTop 事件
 */
interface OnScrollToTop {
    fun scrollToTop(view: View)
}

/**
 * 获取显示叶子列表的 Fragment
 */
interface LeafListFragmentGetter {
    fun getFragment(position: Int): BaseListFragment<*>?
}