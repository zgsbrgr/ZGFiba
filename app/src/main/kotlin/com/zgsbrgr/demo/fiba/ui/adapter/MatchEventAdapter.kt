package com.zgsbrgr.demo.fiba.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.zgsbrgr.demo.fiba.databinding.GameInfoAwayBinding
import com.zgsbrgr.demo.fiba.databinding.GameInfoHomeBinding
import com.zgsbrgr.demo.fiba.domain.MatchEvent
import com.zgsbrgr.demo.fiba.domain.Teams

class MatchEventAdapter :
    ListAdapter<MatchEvent, MatchEventAdapter.MatchEventViewHolder>(MatchEventDiffUtilCallback()) {

    override fun onBindViewHolder(
        holder: MatchEventViewHolder,
        position: Int
    ) {
        holder.bind(getItem(position))
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MatchEventViewHolder {
        return MatchEventViewHolder.from(parent, viewType)
    }

    override fun getItemViewType(position: Int): Int {
        return currentList[position].team.ordinal
    }

    class MatchEventViewHolder private constructor(
        private val binding: ViewBinding,
        private val context: Context
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: MatchEvent) {
            if (binding is GameInfoAwayBinding) {
                binding.icon.setImageResource(item.icon)
                binding.label.text = context.resources.getString(item.event)
            } else if (binding is GameInfoHomeBinding) {
                binding.icon.setImageResource(item.icon)
                binding.label.text = context.resources.getString(item.event)
            }
        }

        companion object {
            fun from(parent: ViewGroup, viewType: Int): MatchEventViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding =
                    if (viewType == Teams.AWAY.ordinal)
                        GameInfoAwayBinding.inflate(layoutInflater, parent, false)
                    else
                        GameInfoHomeBinding.inflate(layoutInflater, parent, false)
                return MatchEventViewHolder(binding, parent.context)
            }
        }
    }

    class MatchEventDiffUtilCallback : DiffUtil.ItemCallback<MatchEvent>() {
        override fun areContentsTheSame(oldItem: MatchEvent, newItem: MatchEvent): Boolean {
            return oldItem == newItem
        }

        override fun areItemsTheSame(oldItem: MatchEvent, newItem: MatchEvent): Boolean {
            return oldItem.id == newItem.id
        }
    }
}
