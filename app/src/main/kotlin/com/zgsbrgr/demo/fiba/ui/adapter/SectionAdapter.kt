package com.zgsbrgr.demo.fiba.ui.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.zgsbrgr.demo.fiba.R
import com.zgsbrgr.demo.fiba.databinding.ItemSectionBinding
import com.zgsbrgr.demo.fiba.domain.Section
import com.zgsbrgr.demo.fiba.ui.adapter.decor.SpaceItemDecoration

class SectionAdapter(
    private val clickListener: SectionClickListener<in Any>
) : ListAdapter<Section, SectionAdapter.SectionViewHolder>(SectionDiffCallback()) {

    private val viewPool: RecyclerView.RecycledViewPool = RecyclerView.RecycledViewPool()

    override fun onBindViewHolder(holder: SectionViewHolder, position: Int) {
        holder.bind(getItem(position), clickListener)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SectionViewHolder {
        return SectionViewHolder.from(parent, viewPool)
    }

    class SectionViewHolder private constructor(
        private val binding: ItemSectionBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Section, clickListener: SectionClickListener<in Any>) {
            binding.item = item
            val adapter = MatchAdapter(
                MatchItemClickListener { match, imageView ->
                    Log.d("Section List", "match item clicked with id: ${match.id} and shared image ${imageView.id}")
                    clickListener.onClick(match, item.section, imageView)
                }
            )
            binding.gamesRv.adapter = adapter
            adapter.submitList(item.matches)
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup, viewPool: RecyclerView.RecycledViewPool): SectionViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ItemSectionBinding.inflate(layoutInflater, parent, false)
                binding.gamesRv.apply {
                    layoutManager = LinearLayoutManager(binding.gamesRv.context, LinearLayoutManager.HORIZONTAL, false)
                    addItemDecoration(SpaceItemDecoration(R.dimen.space, false))
                    setRecycledViewPool(viewPool)
                }
                return SectionViewHolder(binding)
            }
        }
    }

    class SectionDiffCallback : DiffUtil.ItemCallback<Section>() {
        override fun areContentsTheSame(oldItem: Section, newItem: Section): Boolean {
            return oldItem == newItem
        }

        override fun areItemsTheSame(oldItem: Section, newItem: Section): Boolean {
            return oldItem.id == newItem.id
        }
    }
}

class SectionClickListener<T>(val clickListener: (item: T, sectionTitle: String, imageView: ImageView?) -> Unit) {
    fun onClick(item: T, sectionTitle: String, imageView: ImageView?) = clickListener(item, sectionTitle, imageView)
}
