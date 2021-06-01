package com.xiangaoole.android.wanandroid.ui.wechat

import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.RecyclerView
import com.xiangaoole.android.wanandroid.Injection
import com.xiangaoole.android.wanandroid.model.Project
import com.xiangaoole.android.wanandroid.ui.common.BaseListFragment
import kotlinx.coroutines.flow.collectLatest

/**
 * Fragment shows wechat articles list.
 */
class WechatListFragment : BaseListFragment() {

    private val viewModel: WechatArticleListViewModel by viewModels {
        val context = Injection.provideWanAndroidRepository(requireContext())
        val listId = arguments?.getInt(ARG_KEY_ID) ?: -1
        WechatArticleListViewModel.Factory(context, listId)
    }

    override fun provideDataAdapter(): PagingDataAdapter<Project, out RecyclerView.ViewHolder> {
        return WechatListAdapter()
    }

    override suspend fun loadData() {
        viewModel.itemList.collectLatest {
            mAdapter?.submitData(it)
        }
    }

    companion object {
        private const val ARG_KEY_ID: String = "list_id"

        fun newInstance(id: Int): WechatListFragment {
            return WechatListFragment().apply {
                arguments = bundleOf(Pair(ARG_KEY_ID, id))
            }
        }
    }
}