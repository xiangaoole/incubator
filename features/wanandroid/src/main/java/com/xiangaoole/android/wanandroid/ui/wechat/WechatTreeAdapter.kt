package com.xiangaoole.android.wanandroid.ui.wechat

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.xiangaoole.android.wanandroid.model.WechatTree
import com.xiangaoole.android.wanandroid.model.Wechat
import com.xiangaoole.android.wanandroid.ui.common.BaseListFragment

class WechatTreeAdapter(
    private val fragmentManager: FragmentManager,
    lifecycle: Lifecycle,
    private val itemTree: List<WechatTree> = emptyList()
) : FragmentStateAdapter(fragmentManager, lifecycle) {
    override fun getItemCount(): Int {
        return itemTree.size
    }

    override fun createFragment(position: Int): Fragment {
        return WechatListFragment.newInstance(itemTree[position].id)
    }

    /**
     * It is a hack. Get the Fragment at position.
     * The fragment tag added by ViewPager2 is "f0" to "fx". The tag may change
     * in any further version!!!
     */
    fun getFragment(position: Int): BaseListFragment<Wechat>? {
        if (itemCount == 0) return null
        @Suppress("UNCHECKED_CAST")
        return fragmentManager.findFragmentByTag("f$position") as? BaseListFragment<Wechat>
    }
}