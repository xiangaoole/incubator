package com.xiangaoole.android.wanandroid.ui.wechat

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.android.material.tabs.TabLayoutMediator
import com.xiangaoole.android.wanandroid.Injection
import com.xiangaoole.android.wanandroid.R
import com.xiangaoole.android.wanandroid.databinding.FragmentProjectBinding
import com.xiangaoole.android.wanandroid.model.ProjectTree
import com.xiangaoole.android.wanandroid.ui.WanAndroidActivity
import com.xiangaoole.android.wanandroid.ui.common.WechatArticleTreeViewModel
import com.xiangaoole.android.wanandroid.util.bindView
import com.xiangaoole.android.wanandroid.util.fromHtml

class WechatFragment : Fragment(R.layout.fragment_project),
    WanAndroidActivity.ChildFragmentInterface {
    private val binding by bindView(FragmentProjectBinding::bind)
    private val viewModel: WechatArticleTreeViewModel by viewModels {
        val repository = Injection.provideWanAndroidRepository(requireContext())
        WechatArticleTreeViewModel.ViewModelFactory(repository)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.dataList.observe(viewLifecycleOwner) { dataList ->
            if (dataList != null) {
                binding.tvNoData.isVisible = false
                initPagerView(dataList)
            }
        }
    }

    private fun initPagerView(dataTree: List<ProjectTree>) {
        binding.pager.adapter =
            WechatTreeAdapter(childFragmentManager, viewLifecycleOwner.lifecycle, dataTree)
        TabLayoutMediator(binding.tabLayout, binding.pager) { tab, position ->
            tab.text = dataTree[position].name.fromHtml()
        }.attach()
    }

    override fun scrollToTop(view: View) {
        val pager = binding.pager
        val adapter = pager.adapter as? WechatTreeAdapter
        adapter?.getFragment(pager.currentItem)?.scrollToTop(view)
    }
}