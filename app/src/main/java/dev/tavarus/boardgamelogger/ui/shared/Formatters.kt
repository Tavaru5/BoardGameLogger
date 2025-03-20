package dev.tavarus.boardgamelogger.ui.shared

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import dev.tavarus.boardgamelogger.R
import dev.tavarus.boardgamelogger.domain.PlayTime
import dev.tavarus.boardgamelogger.domain.PlayerCount

@Composable
fun PlayTime.formatText() = if (min == 0 || max == 0) {
    if (average >= 60) {
        stringResource(R.string.play_time_single_hours_format, average.minToHourString())
    } else {
        stringResource(R.string.play_time_single_minutes_format, average)
    }
} else {
    if (min >= 60 && max >= 90) {
        stringResource(
            R.string.play_time_range_hours_format,
            min.minToHourString(),
            min.minToHourString(),
        )
    } else {
        stringResource(R.string.play_time_range_minutes_format, min, max)
    }
}

fun Int.minToHourString() = if (this.mod(60) == 0) {
    (this / 60).toString()
} else {
    (this / 60.0).toString()
}


@Composable
fun PlayerCount.formatText() = if (min == max) {
    if (min == 1) {
        stringResource(R.string.player_count_solo)
    } else {
        stringResource(R.string.player_count_single_format, min)
    }
} else {
    stringResource(R.string.player_count_range_format, min, max)
}
