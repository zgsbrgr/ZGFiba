package com.zgsbrgr.demo.fiba.domain

import android.os.Parcelable
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.zgsbrgr.demo.fiba.R
import kotlinx.parcelize.Parcelize
import java.util.Random
import java.util.UUID

@Parcelize
data class Match(
    val id: String = UUID.randomUUID().toString(),
    val date: String,
    val tag: String,
    val thumb: String?,
    val home: Team,
    val away: Team
) : Parcelable

data class MatchEvent(
    val id: String,
    val event: Int,
    val team: Teams,
    val icon: Int
)

enum class Teams {
    HOME, AWAY
}

enum class MatchEventTeam(val team: Teams, @StringRes val eventRes: Int, @DrawableRes val iconRes: Int) {
    HOME_TRIPLE(Teams.HOME, R.string.three_pointer_home, R.drawable.three_pointer),
    AWAY_TRIPLE(Teams.AWAY, R.string.three_pointer_away, R.drawable.three_pointer),
    HOME_DOUBLE(Teams.HOME, R.string.two_pointer_home, R.drawable.two_pointer),
    AWAY_DOUBLE(Teams.AWAY, R.string.two_pointer_away, R.drawable.two_pointer),
    HOME_FAULT(Teams.HOME, R.string.fault_home, R.drawable.fault),
    AWAY_FAULT(Teams.AWAY, R.string.fault_away, R.drawable.fault),
    HOME_TIMEOUT(Teams.HOME, R.string.timeout_home, R.drawable.timeout),
    AWAY_TIMEOUT(Teams.AWAY, R.string.timeout_away, R.drawable.timeout);

    companion object {
        private val PRNG = Random()

        fun randomMatchEvent(): MatchEventTeam {
            val matchEvents: Array<MatchEventTeam> = values()
            return matchEvents[PRNG.nextInt(matchEvents.size)]
        }
    }
}
