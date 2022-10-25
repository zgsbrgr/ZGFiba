package com.zgsbrgr.demo.fiba.ui.detail

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.zgsbrgr.demo.fiba.MyActivityViewModel
import com.zgsbrgr.demo.fiba.R
import com.zgsbrgr.demo.fiba.databinding.TeamStatBinding
import com.zgsbrgr.demo.fiba.domain.Teams
import com.zgsbrgr.demo.fiba.ui.adapter.StatAdapter
import com.zgsbrgr.demo.fiba.ui.adapter.StatData
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.lang.Thread.State

@AndroidEntryPoint
class TeamStat : Fragment() {

    private lateinit var viewBinding: TeamStatBinding
    private val viewModel by viewModels<TeamStatViewModel>()
    private val activityViewModel by activityViewModels<MyActivityViewModel>()

    private val args by navArgs<TeamStatArgs>()

    private val statDataList = mutableListOf<StatData>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewBinding = TeamStatBinding.inflate(inflater, container, false)
        viewBinding.teamStatRv.apply {
            layoutManager = LinearLayoutManager(requireActivity(), LinearLayoutManager.VERTICAL, false)
        }
        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewBinding.lifecycleOwner = this.viewLifecycleOwner

        viewBinding.homeTeam.text = args.homeTeam.team
        viewBinding.awayTeam.text = args.awayTeam.team

        val adapter = StatAdapter()
        val headerList = resources.getStringArray(R.array.player_stat_header)
        val headerItem: StatData = StatData(
            label = headerList[0],
            items = headerList.toList().subList(1, headerList.size - 1)
        )
        viewBinding.teamStatRv.adapter = adapter

        viewLifecycleOwner.lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                activityViewModel.isOffline.collect { notConnected ->
                    if (notConnected)
                        Toast.makeText(
                            requireActivity(),
                            resources.getString(R.string.not_connected),
                            Toast.LENGTH_SHORT
                        ).show()
                    else
                        viewModel.uiState.collect {
                            when (it) {
                                is TeamStatUiState.Loading -> {}
                                is TeamStatUiState.Empty -> {}
                                is TeamStatUiState.Players -> {
                                    viewBinding.homeTeam.isChecked = true
                                }
                            }
                        }
                }
            }
        }

        viewBinding.homeTeam.setOnCheckedChangeListener { _, b ->
            statDataList.clear()
            if (b) {
                statDataList.add(0, headerItem)
                statDataList.addAll(viewModel.changeTeamStatData(Teams.HOME))
            } else {
                statDataList.add(0, headerItem)
                statDataList.addAll(viewModel.changeTeamStatData(Teams.AWAY))
            }
            adapter.submitList(statDataList)
            adapter.notifyItemRangeChanged(1, adapter.itemCount)

            val handler = Handler(Looper.getMainLooper())
            handler.post {
                viewBinding.teamStatRv.scrollToPosition(0)
            }
        }
    }

    companion object {
        const val TAG = "TeamStat"
    }
}
