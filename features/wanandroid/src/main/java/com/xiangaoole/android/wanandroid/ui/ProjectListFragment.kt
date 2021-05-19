package com.xiangaoole.android.wanandroid.ui

import android.os.Bundle
import android.view.View
import androidx.core.content.res.ResourcesCompat
import androidx.core.text.HtmlCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.xiangaoole.android.wanandroid.Injection
import com.xiangaoole.android.wanandroid.R
import com.xiangaoole.android.wanandroid.databinding.FragmentProjectListBinding
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class ProjectListFragment : Fragment(R.layout.fragment_project_list) {
    private var _binding: FragmentProjectListBinding? = null
    private val binding: FragmentProjectListBinding get() = _binding!!

    private lateinit var viewModel: ProjectListViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        _binding = FragmentProjectListBinding.bind(requireView())

        viewModel = ViewModelProvider(this, Injection.provideProjectListVIewModelFactory())
            .get(ProjectListViewModel::class.java)
        binding.recyclerView.apply {
            val decoration = DividerItemDecoration(context, DividerItemDecoration.VERTICAL)
            addItemDecoration(decoration)
            adapter = initAdapter()
        }

        binding.fabButton.setOnClickListener(::scrollToTop)
    }

    private fun initAdapter(): ProjectAdapter {
        val adapter = ProjectAdapter()
        lifecycleScope.launch {
            viewModel.projectList.collectLatest {
                adapter.submitData(it)
            }
        }
        return adapter
    }

    private fun scrollToTop(view: View) {
        binding.recyclerView.run {
            if ((layoutManager as LinearLayoutManager).findFirstVisibleItemPosition() > SMOOTH_SCROLL_THRESHOLD) {
                scrollToPosition(0)
            } else {
                smoothScrollToPosition(0)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        const val SMOOTH_SCROLL_THRESHOLD = 20
    }
}