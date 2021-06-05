package com.xiangaoole.android.wanandroid.ui.home

import androidx.fragment.app.viewModels
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.RecyclerView
import com.xiangaoole.android.wanandroid.Injection
import com.xiangaoole.android.wanandroid.model.HomeArticle
import com.xiangaoole.android.wanandroid.ui.common.BaseListFragment
import kotlinx.coroutines.flow.collectLatest

/**
 * Fragment show the Home page.
 */
class HomeFragment : BaseListFragment<HomeArticle>() {

    private val viewModel: HomeViewModel by viewModels {
        val repository = Injection.provideWanAndroidRepository(requireContext())
        HomeViewModel.Factory(repository)
    }

    override fun provideDataAdapter(): PagingDataAdapter<HomeArticle, out RecyclerView.ViewHolder> {
        return HomeArticleListAdapter()
    }

    override suspend fun loadData() {
        viewModel.itemList.collectLatest {
            mAdapter?.submitData(it)
        }
    }
}