package com.xiangaoole.android.wanandroid.ui

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.xiangaoole.android.wanandroid.model.ProjectTree

class ProjectTreeAdapter(
    fragment: Fragment,
    private val projectTrees: List<ProjectTree> = emptyList()
) : FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int {
        return projectTrees.size
    }

    override fun createFragment(position: Int): Fragment {
        return ProjectListFragment(projectTrees[position].id)
    }
}