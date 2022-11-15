package com.zgsbrgr.demo.fiba.ui.detail.player

import android.os.Build
import android.os.Build.VERSION
import android.os.Bundle
import android.os.Parcelable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.google.android.material.snackbar.Snackbar
import com.zgsbrgr.demo.fiba.MyActivityViewModel
import com.zgsbrgr.demo.fiba.R
import com.zgsbrgr.demo.fiba.databinding.PlayerPageBinding
import com.zgsbrgr.demo.fiba.domain.Team
import com.zgsbrgr.demo.fiba.util.formatPlayerHeightAndAgeToSetInTextView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class PlayerInfo : Fragment() {

    private var _viewBinding: PlayerPageBinding? = null
    private val viewBinding get() = _viewBinding!!

    private val viewModel by viewModels<PlayerInfoViewModel>()
    private val activityViewModel by activityViewModels<MyActivityViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _viewBinding = PlayerPageBinding.inflate(inflater, container, false)
        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val team =
            arguments?.parcelable<Team>("team")

        team?.let {
            viewBinding.teamName.text = it.team
        }
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    activityViewModel.isOffline.collect { notConnected ->
                        Log.d("connected", notConnected.toString())
                        if (notConnected) {
                            Snackbar
                                .make(
                                    viewBinding.root,
                                    resources.getString(R.string.not_connected),
                                    Snackbar.LENGTH_LONG
                                )
                                .show()
                        }
                    }
                }
                launch {
                    viewModel.uiState.collect {
                        it.player?.let { player ->
                            viewBinding.playerName.text = player.player
                            viewBinding.playerPosition.text =
                                resources.getString(R.string.player_number_and_position, "#33", player.playedPosition)
                            viewBinding.ppgValue.text = player.pointsAvg.toString()
                            viewBinding.rpgValue.text = player.reboundsAvg.toString()
                            viewBinding.apgValue.text = player.assistsAvg.toString()
                            viewBinding.pieValue.text = player.efficiency.toString()
                            viewBinding.playerData.text =
                                viewBinding.playerData.context
                                    .formatPlayerHeightAndAgeToSetInTextView(player.height, player.age)
                            viewBinding.gpValue.text = player.gamesPlayed.toString()
                            viewBinding.wlValue.text = player.winLose
                            viewBinding.p2Value.text = player.twoPointsPercentage
                            viewBinding.p3Value.text = player.threePointsPercentage
                            viewBinding.fgValue.text = player.fieldGoalsPercentage
                            viewBinding.ftValue.text = player.freeThrowPercentage
                        }
                    }
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _viewBinding = null
    }
}

inline fun <reified T : Parcelable> Bundle.parcelable(key: String): T? = when {
    VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU -> getParcelable(key, T::class.java)
    else -> @Suppress("DEPRECATION") getParcelable(key) as? T
}
