package com.xiangaoole.android.wanandroid.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.tabs.TabLayoutMediator
import com.xiangaoole.android.wanandroid.Injection
import com.xiangaoole.android.wanandroid.R
import com.xiangaoole.android.wanandroid.databinding.FragmentProjectBinding
import com.xiangaoole.android.wanandroid.util.fromHtml

/**
 * [Fragment] for showing projects of different chapter using [ViewPager2].
 */
class ProjectFragment : Fragment(R.layout.fragment_project) {
    private var _binding: FragmentProjectBinding? = null
    private val binding: FragmentProjectBinding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentProjectBinding.bind(view)

        val viewModel: ProjectTreeViewModel =
            ViewModelProvider(
                this,
                Injection.providerProjectTreeViewModelFactory(requireContext())
            ).get(ProjectTreeViewModel::class.java)

        viewModel.projectTrees.observe(viewLifecycleOwner) { projectTrees ->
            if (projectTrees != null) {
                binding.pager.adapter = ProjectTreeAdapter(this, projectTrees)
                TabLayoutMediator(binding.tabLayout, binding.pager) { tab, position ->
                    tab.text = projectTrees[position].name.fromHtml()
                }.attach()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}