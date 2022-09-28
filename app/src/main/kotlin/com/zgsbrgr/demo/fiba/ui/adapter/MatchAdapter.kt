
@file:Suppress("MagicNumber")

package com.zgsbrgr.demo.fiba.ui.adapter

import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.Target
import com.zgsbrgr.demo.fiba.R
import com.zgsbrgr.demo.fiba.databinding.ItemMatchBinding
import com.zgsbrgr.demo.fiba.domain.Match

class MatchAdapter(
    private val clickListener: MatchItemClickListener
) : ListAdapter<Match, MatchAdapter.MatchViewHolder>(MatchDiffUtilCallback()) {

    override fun onBindViewHolder(holder: MatchViewHolder, position: Int) {
        holder.bind(getItem(position), clickListener)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MatchViewHolder {
        return MatchViewHolder.from(parent)
    }

    class MatchViewHolder private constructor(
        private val binding: ItemMatchBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Match, clickListener: MatchItemClickListener) {
            binding.itemMatch = item
            binding.preview.transitionName = item.thumb
            binding.root.setOnClickListener {
                clickListener.onClick(item, binding.preview)
            }
            binding.awayTeamPoint.text = item.away.points.toString()
            binding.homeTeamPoint.text = item.home.points.toString()
            if (item.away.winner) {
                binding.awayTeam.typeface = Typeface.DEFAULT_BOLD
                binding.awayTeamPoint.typeface = Typeface.DEFAULT_BOLD
            } else {
                binding.awayTeam.typeface = Typeface.DEFAULT
                binding.awayTeamPoint.typeface = Typeface.DEFAULT
                binding.awayTeam.alpha = 0.8f
                binding.awayTeamPoint.alpha = 0.8f
            }

            if (item.home.winner) {
                binding.homeTeam.typeface = Typeface.DEFAULT_BOLD
                binding.homeTeamPoint.typeface = Typeface.DEFAULT_BOLD
            } else {
                binding.homeTeam.typeface = Typeface.DEFAULT
                binding.homeTeamPoint.typeface = Typeface.DEFAULT
                binding.homeTeam.alpha = 0.8f
                binding.homeTeamPoint.alpha = 0.8f
            }

            Glide.with(binding.preview.context)
                .load(item.thumb)
                .error(R.drawable.placeholder)
                .override(Target.SIZE_ORIGINAL)
                .into(binding.preview)

            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): MatchViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ItemMatchBinding.inflate(layoutInflater, parent, false)
                return MatchViewHolder(binding)
            }
        }
    }

    class MatchDiffUtilCallback : DiffUtil.ItemCallback<Match>() {
        override fun areContentsTheSame(oldItem: Match, newItem: Match): Boolean {
            return oldItem == newItem
        }

        override fun areItemsTheSame(oldItem: Match, newItem: Match): Boolean {
            return oldItem.id == newItem.id
        }
    }
}

class MatchItemClickListener(val clickListener: (item: Match, imageView: ImageView) -> Unit) {
    fun onClick(item: Match, imageView: ImageView) = clickListener(item, imageView)
}

@BindingAdapter("dateText")
fun matchDate(dateTextView: TextView, str: String?) {
    str?.let {
        dateTextView.text = dateTextView.context.resources.getString(R.string.date_pl, it)
    }
}

@BindingAdapter("winnerBold")
fun setStyleForWinner(textView: TextView, isWinner: Boolean?) {
    isWinner?.let {
        if (it)
            textView.typeface = Typeface.DEFAULT_BOLD
        else
            textView.typeface = Typeface.DEFAULT
    }
}
