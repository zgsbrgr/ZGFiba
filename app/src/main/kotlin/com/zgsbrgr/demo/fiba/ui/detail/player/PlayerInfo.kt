package com.zgsbrgr.demo.fiba.ui.detail.player

import android.os.Build
import android.os.Build.VERSION
import android.os.Bundle
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.zgsbrgr.demo.fiba.R
import com.zgsbrgr.demo.fiba.databinding.PlayerPageBinding
import com.zgsbrgr.demo.fiba.domain.Player
import com.zgsbrgr.demo.fiba.domain.Team
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class PlayerInfo : Fragment() {

    private lateinit var viewBinding: PlayerPageBinding
    private val viewModel by viewModels<PlayerInfoViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewBinding = PlayerPageBinding.inflate(inflater, container, false)
        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewBinding.lifecycleOwner = this.viewLifecycleOwner

        val team =
            arguments?.parcelable<Team>("team")

        viewBinding.teamName = team?.team

        viewLifecycleOwner.lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect {

                    it.player?.let { currentPlayer ->
                        viewBinding.player = currentPlayer
                    }
                }
            }
        }
    }
}

@BindingAdapter("playerPosition")
fun formatPlayerNumberAndPosition(textView: TextView, playerPosition: String?) {
    playerPosition?.let {
        textView.text = textView.context.resources.getString(R.string.player_number_and_position, "#33", it)
    }
}

@BindingAdapter("formatPlayerStat")
fun formatPlayerStatToSetInTextView(textView: TextView, playerStat: Double?) {
    playerStat?.let { stat ->
        textView.text = stat.toString()
    }
}

@BindingAdapter("player")
fun formatPlayerHeightAndAgeToSetInTextView(textView: TextView, player: Player?) {
    player?.let { selectedPlayer ->
        val heightItems = selectedPlayer.height.split("m")
        textView.text =
            textView.context.resources.getString(
                R.string.player_data, heightItems[0], heightItems[1], selectedPlayer.age
            )
    }
}

@BindingAdapter("playedGames")
fun formatGamesPlayedToSetInTextView(textView: TextView, gamesPlayed: Int?) {
    gamesPlayed?.let {
        textView.text = it.toString()
    }
}

inline fun <reified T : Parcelable> Bundle.parcelable(key: String): T? = when {
    VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU -> getParcelable(key, T::class.java)
    else -> @Suppress("DEPRECATION") getParcelable(key) as? T
}
