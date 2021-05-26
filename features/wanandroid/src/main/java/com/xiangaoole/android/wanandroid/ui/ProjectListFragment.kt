package com.xiangaoole.android.wanandroid.ui

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.paging.CombinedLoadStates
import androidx.paging.LoadState
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.xiangaoole.android.wanandroid.Injection
import com.xiangaoole.android.wanandroid.R
import com.xiangaoole.android.wanandroid.api.WanAndroidService.Companion.COMPLETE_PROJECT_CID
import com.xiangaoole.android.wanandroid.databinding.FragmentProjectListBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class ProjectListFragment(private val cid: Int = COMPLETE_PROJECT_CID) : Fragment(R.layout.fragment_project_list) {
    private var _binding: FragmentProjectListBinding? = null
    private val binding: FragmentProjectListBinding get() = _binding!!
    private val adapter = ProjectAdapter()

    private lateinit var viewModel: ProjectListViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        _binding = FragmentProjectListBinding.bind(requireView())

        viewModel =
            ViewModelProvider(this, Injection.provideProjectListViewModelFactory(requireContext(), cid))
                .get(ProjectListViewModel::class.java)

        val decoration = DividerItemDecoration(context, DividerItemDecoration.VERTICAL)
        binding.recyclerView.addItemDecoration(decoration)
        binding.recyclerView.adapter = initAdapter()

        binding.fabButton.setOnClickListener(::scrollToTop)
        binding.retryButton.setOnClickListener { adapter.retry() }
    }

    private fun initAdapter(): RecyclerView.Adapter<RecyclerView.ViewHolder> {
        lifecycleScope.launch {
            viewModel.projectList.collectLatest {
                adapter.submitData(it)
            }
        }

        adapter.addLoadStateListener(loadStateListener)

        return adapter.withLoadStateHeaderAndFooter(
            ProjectLoadStateAdapter { adapter.retry() },
            ProjectLoadStateAdapter { adapter.retry() }
        )
    }

    private val loadStateListener: (CombinedLoadStates) -> Unit = { loadState ->
        // show empty list
        val isListEmpty = loadState.refresh is LoadState.NotLoading && adapter.itemCount == 0
        binding.emptyResultText.isVisible = isListEmpty

        // show list
        binding.recyclerView.isVisible = loadState.source.refresh is LoadState.NotLoading

        // show loading progress bar
        binding.progressBar.isVisible = loadState.source.refresh is LoadState.Loading

        // show retry button
        binding.retryButton.isVisible = loadState.source.refresh is LoadState.Error

        // Toast error state
        val errorState = loadState.source.prepend as? LoadState.Error
            ?: loadState.source.append as? LoadState.Error
            ?: loadState.prepend as? LoadState.Error
            ?: loadState.append as? LoadState.Error
        errorState?.let {
            Toast.makeText(
                context, "\uD83D\uDE28 Wooops ${it.error}", Toast.LENGTH_LONG
            ).show()
        }
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
        adapter.removeLoadStateListener(loadStateListener)
    }

    companion object {
        const val SMOOTH_SCROLL_THRESHOLD = 20
    }
}