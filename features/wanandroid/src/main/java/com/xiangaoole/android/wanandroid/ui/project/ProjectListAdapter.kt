package com.xiangaoole.android.wanandroid.ui.project

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.xiangaoole.android.wanandroid.databinding.ListItemProjectBinding
import com.xiangaoole.android.wanandroid.model.Project
import com.xiangaoole.android.wanandroid.ui.ArticleActivity
import timber.log.Timber

class ProjectAdapter : PagingDataAdapter<Project, ProjectViewHolder>(PROJECT_DIFF) {

    companion object {
        private val PROJECT_DIFF = object : DiffUtil.ItemCallback<Project>() {
            override fun areItemsTheSame(oldItem: Project, newItem: Project): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Project, newItem: Project): Boolean {
                return oldItem == newItem
            }

        }
    }

    override fun onBindViewHolder(holder: ProjectViewHolder, position: Int) {
        val project = getItem(position)
        holder.bind(project)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProjectViewHolder {
        return ProjectViewHolder.create(parent)
    }
}

class ProjectViewHolder private constructor(
    private val binding: ListItemProjectBinding
) : RecyclerView.ViewHolder(binding.root) {

    init {
        binding.cardView.setOnClickListener(::onClick)
    }

    fun bind(project: Project?) {
        if (project != null) {
            binding.project = project
            binding.executePendingBindings()
        }
    }

    private fun onClick(view: View) {
        binding.project?.let {
            ArticleActivity.start(
                view.context,
                id = it.id,
                title = it.title,
                url = it.link
            )
        } ?: Timber.w("onClick when project is null")
    }

    companion object {
        fun create(parent: ViewGroup): ProjectViewHolder {
            val inflater = LayoutInflater.from(parent.context)
            val binding = ListItemProjectBinding.inflate(inflater, parent, false)
            return ProjectViewHolder(binding)
        }
    }
}