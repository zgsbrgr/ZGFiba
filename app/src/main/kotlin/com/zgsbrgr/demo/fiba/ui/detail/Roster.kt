package com.zgsbrgr.demo.fiba.ui.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.zgsbrgr.demo.fiba.databinding.RosterBinding
import com.zgsbrgr.demo.fiba.ui.adapter.RosterAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class Roster : Fragment() {

    private lateinit var viewBinding: RosterBinding
    private val viewModel by viewModels<RosterViewModel>()

    private val args by navArgs<RosterArgs>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewBinding = RosterBinding.inflate(inflater, container, false)
        viewBinding.rosterRv.apply {
            layoutManager = LinearLayoutManager(
                requireActivity(),
                LinearLayoutManager.VERTICAL,
                false
            )
            addItemDecoration(DividerItemDecoration(requireActivity(), DividerItemDecoration.VERTICAL))
        }
        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewBinding.lifecycleOwner = this.viewLifecycleOwner

        viewBinding.homeTeam.text = args.homeTeamName
        viewBinding.awayTeam.text = args.awayTeamName

        val adapter = RosterAdapter()
        viewBinding.rosterRv.adapter = adapter
        viewLifecycleOwner.lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.rosterUIState.collect {
                    when (it) {
                        is RosterUiState.Loading -> {}
                        is RosterUiState.Empty -> {}
                        is RosterUiState.Rosters -> {
                            adapter.submitList(
                                it.homeAndAwayRosters
                            )
                        }
                    }
                }
            }
        }
    }

    companion object {
        const val TAG = "Roster"
    }
}
