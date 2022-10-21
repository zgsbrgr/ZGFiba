package com.zgsbrgr.demo.fiba.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.zgsbrgr.demo.fiba.databinding.ItemStatBinding

class StatDataAdapter: ListAdapter<String,StatDataAdapter.StatDataViewHolder>(StatDataDiffUtilItemCallback()) {

    override fun onBindViewHolder(holder: StatDataViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StatDataViewHolder {
        return StatDataViewHolder.from(parent)
    }

    class StatDataViewHolder private constructor(private val binding: ItemStatBinding): RecyclerView.ViewHolder(binding.root) {

        fun bind(item: String) {
            binding.statText.text = item
        }

        companion object {
            fun from(parent: ViewGroup): StatDataViewHolder {
                val inflater: LayoutInflater = LayoutInflater.from(parent.context)
                val binding = ItemStatBinding.inflate(inflater, parent, false)
                return StatDataViewHolder(binding)
            }
        }

    }

    class StatDataDiffUtilItemCallback(): DiffUtil.ItemCallback<String>() {
        override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem == newItem
        }

        override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem == newItem
        }
    }
}