package com.xiangaoole.android.wanandroid.ui.wechat

import androidx.fragment.app.viewModels
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.RecyclerView
import com.xiangaoole.android.wanandroid.Injection
import com.xiangaoole.android.wanandroid.model.Project
import com.xiangaoole.android.wanandroid.ui.common.BaseListFragment
import kotlinx.coroutines.flow.collectLatest

class WechatListFragment(private val listId: Int = -1)
    : BaseListFragment() {

    private val viewModel: WechatArticleListViewModel by viewModels {
        val context = Injection.provideWanAndroidRepository(requireContext())
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
}