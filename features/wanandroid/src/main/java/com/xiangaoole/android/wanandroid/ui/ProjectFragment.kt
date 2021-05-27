package com.xiangaoole.android.wanandroid.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.android.material.tabs.TabLayoutMediator
import com.xiangaoole.android.wanandroid.Injection
import com.xiangaoole.android.wanandroid.R
import com.xiangaoole.android.wanandroid.databinding.FragmentProjectBinding
import com.xiangaoole.android.wanandroid.model.ProjectTree
import com.xiangaoole.android.wanandroid.util.bindView
import com.xiangaoole.android.wanandroid.util.fromHtml

/**
 * [Fragment] for showing projects of different chapter using [ViewPager2].
 */
class ProjectFragment : Fragment(R.layout.fragment_project),
    WanAndroidActivity.ChildFragmentInterface {
    private val binding by bindView(FragmentProjectBinding::bind)
    private val viewModel: ProjectTreeViewModel by viewModels {
        Injection.providerProjectTreeViewModelFactory(requireContext())
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.projectTrees.observe(viewLifecycleOwner) { projectTrees ->
            if (projectTrees != null) {
                initPagerView(projectTrees)
            }
        }
    }

    private fun initPagerView(projectTrees: List<ProjectTree>) {
        binding.pager.adapter =
            ProjectTreeAdapter(childFragmentManager, viewLifecycleOwner.lifecycle, projectTrees)
        TabLayoutMediator(binding.tabLayout, binding.pager) { tab, position ->
            tab.text = projectTrees[position].name.fromHtml()
        }.attach()
    }

    override fun scrollToTop(view: View) {
        val pager = binding.pager
        val adapter = pager.adapter as? ProjectTreeAdapter
        adapter?.getFragment(pager.currentItem)?.scrollToTop(view)
    }
}