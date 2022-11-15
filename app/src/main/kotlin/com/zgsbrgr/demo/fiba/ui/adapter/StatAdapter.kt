package com.zgsbrgr.demo.fiba.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.zgsbrgr.demo.fiba.databinding.StatHeaderBinding

class StatAdapter : ListAdapter<StatData, StatAdapter.StatViewHolder>(StatAdapterItemDiffUtil()) {

    override fun onBindViewHolder(holder: StatViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StatViewHolder {
        return StatViewHolder.from(parent)
    }

    class StatViewHolder private constructor(
        private val binding: StatHeaderBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: StatData) {
            binding.headerText.text = item.label
            binding.statRv.apply {
                layoutManager = LinearLayoutManager(binding.statRv.context, LinearLayoutManager.HORIZONTAL, false)
            }
            val adapter = StatDataAdapter()
            binding.statRv.adapter = adapter
            adapter.submitList(
                item.items
            )
        }

        companion object {
            fun from(parent: ViewGroup): StatViewHolder {
                val inflater = LayoutInflater.from(parent.context)
                val binding = StatHeaderBinding.inflate(inflater, parent, false)
                return StatViewHolder(binding)
            }
        }
    }

    class StatAdapterItemDiffUtil : DiffUtil.ItemCallback<StatData>() {
        override fun areContentsTheSame(oldItem: StatData, newItem: StatData): Boolean {
            return oldItem == newItem
        }

        override fun areItemsTheSame(oldItem: StatData, newItem: StatData): Boolean {
            return oldItem.label == newItem.label
        }
    }
}

data class StatData(
    val label: String,
    val items: List<String>
)
