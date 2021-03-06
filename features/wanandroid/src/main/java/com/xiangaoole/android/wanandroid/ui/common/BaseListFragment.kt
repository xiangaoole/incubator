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
import com.xiangaoole.android.wanandroid.databinding.FragmentCommonListBinding
import com.xiangaoole.android.wanandroid.util.bindView
import com.xiangaoole.android.wanandroid.widget.CustomToast
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

/**
 * Base Fragment show a RecycleView
 */
abstract class BaseListFragment<T : Any>
    : Fragment(R.layout.fragment_common_list), OnScrollToTop {

    protected val binding by bindView(FragmentCommonListBinding::bind)

    /**
     * This [PagingDataAdapter] reference will be null when [onDestroy]
     */
    protected var mAdapter: PagingDataAdapter<T, out RecyclerView.ViewHolder>? = null
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
    abstract fun provideDataAdapter(): PagingDataAdapter<T, out RecyclerView.ViewHolder>

    /**
     * Load initial data to [mAdapter].
     */
    abstract suspend fun loadData()

    protected open fun initAdapter(adapter: PagingDataAdapter<T, out RecyclerView.ViewHolder>): RecyclerView.Adapter<RecyclerView.ViewHolder> {
        mAdapter = adapter
        mInitAdapterJob = lifecycleScope.launch {
            loadData()
        }

        adapter.addLoadStateListener(loadStateListener)

        return adapter.withLoadStateHeaderAndFooter(
            LeafLoadStateAdapter { adapter.retry() },
            LeafLoadStateAdapter { adapter.retry() }
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
        val errorState =
            loadState.source.refresh as? LoadState.Error
                ?: loadState.source.prepend as? LoadState.Error
                ?: loadState.source.append as? LoadState.Error
                ?: loadState.prepend as? LoadState.Error
                ?: loadState.append as? LoadState.Error
        errorState?.error?.message?.let { msg -> CustomToast(requireContext(), msg).show() }

    }

    companion object {
        const val SMOOTH_SCROLL_THRESHOLD = 20
    }
}