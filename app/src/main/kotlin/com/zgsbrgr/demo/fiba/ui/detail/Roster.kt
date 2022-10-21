package com.zgsbrgr.demo.fiba.ui.detail

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.zgsbrgr.demo.fiba.R
import com.zgsbrgr.demo.fiba.databinding.PlayerDialogBinding
import com.zgsbrgr.demo.fiba.databinding.RosterBinding
import com.zgsbrgr.demo.fiba.domain.Player
import com.zgsbrgr.demo.fiba.domain.Teams
import com.zgsbrgr.demo.fiba.ui.adapter.RosterAdapter
import com.zgsbrgr.demo.fiba.ui.adapter.RosterItemClickListener
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

        viewBinding.homeTeam.text = args.homeTeam.team
        viewBinding.awayTeam.text = args.awayTeam.team

        val adapter = RosterAdapter(
            RosterItemClickListener { playerPosition, homeOrAwayTeam, player ->

                showPlayerDialog(playerPosition, homeOrAwayTeam, player)
            }
        )
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

    private fun showPlayerDialog(playerPosition: Int, homeOrAwayTeam: Teams, player: Player) {
        Log.d(TAG, playerPosition.toString())
        Log.d(TAG, homeOrAwayTeam.name)
        val dialog = BottomSheetDialog(requireActivity())

        val dialogBinding = PlayerDialogBinding.inflate(layoutInflater)
        dialogBinding.player = player
        dialogBinding.navigateToStatIcon.setOnClickListener {
            val bundle = bundleOf(
                "team" to if (homeOrAwayTeam == Teams.HOME) args.homeTeam else args.awayTeam,
                "position" to playerPosition
            )
            this.findNavController().navigate(R.id.playerInfo, bundle)
            dialog.dismiss()
        }
        dialog.setContentView(dialogBinding.root)
        dialog.show()
    }

    companion object {
        const val TAG = "Roster"
    }
}
