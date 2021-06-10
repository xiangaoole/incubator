package com.xiangaoole.android.wanandroid.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.viewModels
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.RecyclerView
import com.xiangaoole.android.wanandroid.Injection
import com.xiangaoole.android.wanandroid.R
import com.xiangaoole.android.wanandroid.adapter.CollectionPagingAdapter
import com.xiangaoole.android.wanandroid.model.CollectionArticle
import com.xiangaoole.android.wanandroid.ui.common.BaseListFragment
import com.xiangaoole.android.wanandroid.viewmodel.CollectionViewModel
import kotlinx.coroutines.flow.collectLatest

class CollectionActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_collection)
    }
}

class CollectionFragment : BaseListFragment<CollectionArticle>() {

    private val viewModel: CollectionViewModel by viewModels {
        val repository = Injection.provideWanAndroidRepository(requireContext())
        CollectionViewModel.Factory(repository)
    }

    override fun provideDataAdapter(): PagingDataAdapter<CollectionArticle, out RecyclerView.ViewHolder> {
        return CollectionPagingAdapter()
    }

    override suspend fun loadData() {
        viewModel.collectionList.collectLatest {
            mAdapter?.submitData(it)
        }
    }
}
