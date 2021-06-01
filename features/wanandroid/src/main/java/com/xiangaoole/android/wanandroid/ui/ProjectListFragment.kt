package com.xiangaoole.android.wanandroid.ui

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.paging.CombinedLoadStates
import androidx.paging.LoadState
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.xiangaoole.android.wanandroid.Injection
import com.xiangaoole.android.wanandroid.R
import com.xiangaoole.android.wanandroid.api.WanAndroidService.Companion.COMPLETE_PROJECT_CID
import com.xiangaoole.android.wanandroid.databinding.FragmentProjectListBinding
import com.xiangaoole.android.wanandroid.util.bindView
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import timber.log.Timber

class ProjectListFragment : Fragment(R.layout.fragment_project_list),
    WanAndroidActivity.ChildFragmentInterface {

    private val binding by bindView(FragmentProjectListBinding::bind)

    private val viewModel: ProjectListViewModel by viewModels {
        val cid: Int = arguments?.getInt(ARG_KEY_CID) ?: -1
        Injection.provideProjectListViewModelFactory(requireContext(), cid)
    }

    protected var mAdapter: ProjectAdapter? = null

    private var mInitAdapterJob: Job? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        val decoration = DividerItemDecoration(context, DividerItemDecoration.VERTICAL)
        binding.recyclerView.addItemDecoration(decoration)
        val adapter = ProjectAdapter()
        binding.recyclerView.adapter = initAdapter(adapter)

        //view.findViewById<Button>(R.id.fabButton).setOnClickListener(::scrollToTop)
        binding.retryButton.setOnClickListener { adapter.retry() }
    }

    private fun initAdapter(adapter: ProjectAdapter): RecyclerView.Adapter<RecyclerView.ViewHolder> {
        mAdapter = adapter
        mInitAdapterJob = lifecycleScope.launch {
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

    override fun scrollToTop(view: View) {
        Timber.d("Harold: scrollToTop")
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

        // avoid leak view
        mAdapter?.removeLoadStateListener(loadStateListener)
        mInitAdapterJob?.cancel()
        mInitAdapterJob = null
        mAdapter = null
    }

    private val loadStateListener: (CombinedLoadStates) -> Unit = { loadState ->
        // show empty list
        val isListEmpty = loadState.refresh is LoadState.NotLoading && mAdapter?.itemCount == 0
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

    companion object {
        const val SMOOTH_SCROLL_THRESHOLD = 20
        const val ARG_KEY_CID = "cid"

        fun newInstance(cid: Int): ProjectListFragment {
            return ProjectListFragment().apply {
                val bundle = Bundle()
                bundle.putInt(ARG_KEY_CID, cid)
                arguments = bundle
            }
        }
    }
}