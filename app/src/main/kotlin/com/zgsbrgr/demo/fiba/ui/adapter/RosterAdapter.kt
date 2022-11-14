package com.zgsbrgr.demo.fiba.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.zgsbrgr.demo.fiba.R
import com.zgsbrgr.demo.fiba.databinding.ItemRosterBinding
import com.zgsbrgr.demo.fiba.domain.Player
import com.zgsbrgr.demo.fiba.domain.Teams

class RosterAdapter(
    private val clickListener: RosterItemClickListener
) : ListAdapter<Pair<Player?, Player?>, RosterAdapter.RosterViewHolder>(RosterDiffUtilCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RosterViewHolder {
        return RosterViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: RosterViewHolder, position: Int) {
        holder.bind(getItem(position), position, clickListener)
    }

    class RosterViewHolder private constructor(
        private val binding: ItemRosterBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Pair<Player?, Player?>, position: Int, clickListener: RosterItemClickListener) {

            if (position % 2 == 0)
                binding.root.setBackgroundColor(
                    ResourcesCompat.getColor(binding.root.resources, R.color.blue, null)
                )
            else
                binding.root.setBackgroundColor(
                    ResourcesCompat.getColor(binding.root.resources, R.color.blue_dark, null)
                )
            item.first?.let { homePlayer ->
                binding.homePlayer.text = homePlayer.player
                binding.homePlayerNr.text = homePlayer.playedPosition
                binding.homePlayer.visibility = View.VISIBLE
                binding.homePlayerNr.visibility = View.VISIBLE
            } ?: kotlin.run {
                binding.homePlayer.visibility = View.GONE
                binding.homePlayerNr.visibility = View.GONE
            }
            item.second?.let { awayPlayer ->
                binding.awayPlayer.text = awayPlayer.player
                binding.awayPlayerNr.text = awayPlayer.playedPosition
                binding.awayPlayer.visibility = View.VISIBLE
                binding.awayPlayerNr.visibility = View.VISIBLE
            } ?: kotlin.run {
                binding.awayPlayer.visibility = View.GONE
                binding.awayPlayerNr.visibility = View.GONE
            }

            binding.homePlayer.setOnClickListener {
                clickListener.onClick(
                    bindingAdapterPosition,
                    Teams.HOME,
                    item.first!!
                )
            }
            binding.awayPlayer.setOnClickListener {
                clickListener.onClick(
                    bindingAdapterPosition,
                    Teams.AWAY,
                    item.second!!
                )
            }
        }

        companion object {
            fun from(parent: ViewGroup): RosterViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ItemRosterBinding.inflate(layoutInflater, parent, false)
                return RosterViewHolder(binding)
            }
        }
    }

    class RosterDiffUtilCallback : DiffUtil.ItemCallback<Pair<Player?, Player?>>() {
        override fun areContentsTheSame(
            oldItem: Pair<Player?, Player?>,
            newItem: Pair<Player?, Player?>
        ): Boolean {
            return oldItem == newItem
        }

        override fun areItemsTheSame(
            oldItem: Pair<Player?, Player?>,
            newItem: Pair<Player?, Player?>
        ): Boolean {
            return (
                oldItem.first?.id == newItem.first?.id &&
                    oldItem.second?.id == newItem.second?.id
                )
        }
    }
}

class RosterItemClickListener(
    val clickListener: (itemPosition: Int, homeOrAwayTeam: Teams, player: Player) -> Unit
) {
    fun onClick(itemPosition: Int, homeOrAwayTeam: Teams, player: Player) =
        clickListener(itemPosition, homeOrAwayTeam, player)
}
