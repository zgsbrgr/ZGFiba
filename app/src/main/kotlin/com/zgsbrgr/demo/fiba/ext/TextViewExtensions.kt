package com.zgsbrgr.demo.fiba.ext

import android.widget.TextView
import com.zgsbrgr.demo.fiba.R
import com.zgsbrgr.demo.fiba.domain.Player

fun TextView.formatPlayerNumberAndPosition(playerPosition: String?) {
    playerPosition?.let {
        this.text = this.context.resources.getString(R.string.player_number_and_position, "#33", it)
    }
}

fun TextView.formatPlayerStatToSetInTextView(playerStat: Double?) {
    playerStat?.let { stat ->
        this.text = stat.toString()
    }
}

fun TextView.formatPlayerHeightAndAgeToSetInTextView(player: Player?) {
    player?.let { selectedPlayer ->
        val heightItems = selectedPlayer.height.split("m")
        this.text =
            this.context.resources.getString(
                R.string.player_data, heightItems[0], heightItems[1], selectedPlayer.age
            )
    }
}

fun TextView.formatGamesPlayedToSetInTextView(gamesPlayed: Int?) {
    gamesPlayed?.let {
        this.text = it.toString()
    }
}

fun TextView.matchDate(str: String?) {
    str?.let {
        this.text = this.context.resources.getString(R.string.date_pl, it)
    }
}
