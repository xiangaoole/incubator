package com.xiangaoole.android.wanandroid.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.fragment.app.viewModels
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.RecyclerView
import com.xiangaoole.android.wanandroid.Injection
import com.xiangaoole.android.wanandroid.R
import com.xiangaoole.android.wanandroid.adapter.CollectionPagingAdapter
import com.xiangaoole.android.wanandroid.databinding.ActivityCollectionBinding
import com.xiangaoole.android.wanandroid.model.CollectionArticle
import com.xiangaoole.android.wanandroid.ui.common.BaseListFragment
import com.xiangaoole.android.wanandroid.viewmodel.CollectionViewModel
import kotlinx.coroutines.flow.collectLatest

class CollectionActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCollectionBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCollectionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.includedToolbar.run {
            setSupportActionBar(toolbar)
            title = getString(R.string.my_collection)
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }
        binding.fabButton.setOnClickListener {
            binding.fragmentContainerViewTag.getFragment<CollectionFragment>()?.scrollToTop(it)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> onBackPressed()
        }
        return super.onOptionsItemSelected(item)
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
