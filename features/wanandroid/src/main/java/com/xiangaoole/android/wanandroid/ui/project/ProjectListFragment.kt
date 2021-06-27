package com.xiangaoole.android.wanandroid.ui.project

import android.os.Bundle
import androidx.fragment.app.viewModels
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.RecyclerView
import com.xiangaoole.android.wanandroid.Injection
import com.xiangaoole.android.wanandroid.model.Project
import com.xiangaoole.android.wanandroid.ui.common.BaseListFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import javax.inject.Inject

/**
 * Fragment show project articles list.
 */
@AndroidEntryPoint
class ProjectListFragment : BaseListFragment<Project>() {

    @Inject lateinit var viewModelFactory: ProjectListViewModel.ProjectListViewModelFactory

    private val viewModel: ProjectListViewModel by viewModels {
        val cid: Int = arguments?.getInt(ARG_KEY_CID) ?: -1
        Injection.provideProjectListViewModelFactory(requireContext(), cid)
    }

    override fun provideDataAdapter(): PagingDataAdapter<Project, out RecyclerView.ViewHolder> {
        return ProjectAdapter()
    }

    override suspend fun loadData() {
        viewModel.projectList.collectLatest {
            mAdapter?.submitData(it)
        }
    }

    companion object {
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