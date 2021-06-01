package com.xiangaoole.android.wanandroid.ui.common

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.paging.CombinedLoadStates
import androidx.paging.LoadState
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.xiangaoole.android.wanandroid.R
import com.xiangaoole.android.wanandroid.databinding.FragmentProjectListBinding
import com.xiangaoole.android.wanandroid.model.Project
import com.xiangaoole.android.wanandroid.ui.ProjectAdapter
import com.xiangaoole.android.wanandroid.ui.ProjectListFragment
import com.xiangaoole.android.wanandroid.ui.ProjectLoadStateAdapter
import com.xiangaoole.android.wanandroid.ui.WanAndroidActivity
import com.xiangaoole.android.wanandroid.util.bindView
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import timber.log.Timber

/**
 * Base Fragment show a RecycleView
 */
abstract class BaseListFragment
    : Fragment(R.layout.fragment_project_list), WanAndroidActivity.ChildFragmentInterface {

    private val binding by bindView(FragmentProjectListBinding::bind)

    /**
     * This [PagingDataAdapter] reference will be null when [onDestroy]
     */
    protected var mAdapter: PagingDataAdapter<Project, out RecyclerView.ViewHolder>? = null
        private set

    private var mInitAdapterJob: Job? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        val decoration = DividerItemDecoration(context, DividerItemDecoration.VERTICAL)
        binding.recyclerView.addItemDecoration(decoration)
        val adapter = provideDataAdapter()
        binding.recyclerView.adapter = initAdapter(adapter)

        binding.retryButton.setOnClickListener { adapter.retry() }
    }

    /**
     * provide [PagingDataAdapter] for the data RecyclerView
     */
    abstract fun provideDataAdapter(): PagingDataAdapter<Project, out RecyclerView.ViewHolder>

    /**
     * Load initial data to [mAdapter].
     */
    abstract suspend fun loadData()

    private fun initAdapter(adapter: PagingDataAdapter<Project, out RecyclerView.ViewHolder>): RecyclerView.Adapter<RecyclerView.ViewHolder> {
        mAdapter = adapter
        mInitAdapterJob = lifecycleScope.launch {
            loadData()
        }

        adapter.addLoadStateListener(loadStateListener)

        return adapter.withLoadStateHeaderAndFooter(
            ProjectLoadStateAdapter { adapter.retry() },
            ProjectLoadStateAdapter { adapter.retry() }
        )
    }

    override fun scrollToTop(view: View) {
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
    }
}