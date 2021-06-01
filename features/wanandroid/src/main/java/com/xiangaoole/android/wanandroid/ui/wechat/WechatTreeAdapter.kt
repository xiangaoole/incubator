package com.xiangaoole.android.wanandroid.ui.wechat

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.xiangaoole.android.wanandroid.model.ProjectTree
import com.xiangaoole.android.wanandroid.ui.common.BaseListFragment

class WechatTreeAdapter(
    private val fragmentManager: FragmentManager,
    lifecycle: Lifecycle,
    private val itemTree: List<ProjectTree> = emptyList()
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
    fun getFragment(position: Int): BaseListFragment? {
        if (itemCount == 0) return null
        return fragmentManager.findFragmentByTag("f$position") as? BaseListFragment
    }
}