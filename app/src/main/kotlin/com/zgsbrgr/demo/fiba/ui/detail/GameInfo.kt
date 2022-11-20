package com.zgsbrgr.demo.fiba.ui.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.zgsbrgr.demo.fiba.databinding.GameInfoBinding
import com.zgsbrgr.demo.fiba.domain.Match
import com.zgsbrgr.demo.fiba.domain.MatchEvent
import com.zgsbrgr.demo.fiba.ui.adapter.MatchEventAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class GameInfo : Fragment() {

    private var _viewBinding: GameInfoBinding? = null
    private val viewBinding get() = _viewBinding!!

    private val viewModel by viewModels<GameInfoViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _viewBinding = GameInfoBinding.inflate(inflater, container, false)
        viewBinding.eventRv.apply {
            layoutManager = LinearLayoutManager(requireActivity(), LinearLayoutManager.VERTICAL, false)
            addItemDecoration(DividerItemDecoration(requireActivity(), DividerItemDecoration.HORIZONTAL))
        }
        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val eventList = mutableListOf<MatchEvent>()

        val adapter = MatchEventAdapter()

        viewBinding.eventRv.adapter = adapter

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect {
                    it.data?.let { event ->
                        eventList.add(event)
                        if (adapter.currentList.isEmpty())
                            adapter.submitList(eventList)

                        adapter.notifyItemInserted(eventList.size)
                        viewBinding.eventRv.scrollToPosition(eventList.size - 1)
                    }

                    if (it.error != null)
                        Toast.makeText(
                            requireActivity(),
                            it.error.toString(),
                            Toast.LENGTH_SHORT
                        )
                            .show()
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.RESUMED) {
                viewModel.loadEvents()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _viewBinding = null
    }

    override fun onPause() {
        super.onPause()
        viewModel.removeUpdates()
    }
}

class GamePagerAdapter(fragment: Fragment, private val match: Match) : FragmentStateAdapter(fragment) {

    override fun createFragment(position: Int): Fragment {

        val fragment =
            when (position) {
                0 -> {
                    val f = Roster()
                    f.arguments = Bundle().apply {
                        putParcelable("homeTeam", match.home)
                        putParcelable("awayTeam", match.away)
                    }
                    f
                }
                1 -> {
                    val f = GameInfo()
                    f.arguments = Bundle().apply {
                        putInt(ARG_OBJECT, position)
                        putString(ARG_MATCH_ID, match.id)
                    }
                    f
                }
                2 -> {
                    val f = TeamStat()
                    f.arguments = Bundle().apply {
                        putParcelable("homeTeam", match.home)
                        putParcelable("awayTeam", match.away)
                    }
                    f
                }
                else -> {
                    throw NoSuchElementException("")
                }
            }
        return fragment
    }

    override fun getItemCount(): Int {
        return 3
    }
}
