package com.xiangaoole.android.wanandroid.ui.common

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter

/**
 * 树结构的列表数据，每棵树有 Branch 列表，每个 Branch 又包含 Leaf 列表。
 * 使用 ViewPage2 来显示 Branch 列表
 *
 * @param B Branch Type of the Tree.
 */
class BaseTreeAdapter<B>(
    private val fragmentManager: FragmentManager,
    lifecycle: Lifecycle,
    private val branches: List<B> = emptyList(),
    private val getInstance: (B) -> Fragment
) : FragmentStateAdapter(fragmentManager, lifecycle), LeafListFragmentGetter {
    override fun getItemCount(): Int {
        return branches.size
    }

    override fun createFragment(position: Int): Fragment {
        return getInstance(branches[position])
    }

    /**
     * It is a hack. Get the Fragment at position.
     * The fragment tag added by ViewPager2 is "f0" to "fx". The tag may change
     * in any further version!!!
     */
    override fun getFragment(position: Int): BaseListFragment<*>? {
        if (itemCount == 0) return null
        return fragmentManager.findFragmentByTag("f$position") as? BaseListFragment<*>
    }
}