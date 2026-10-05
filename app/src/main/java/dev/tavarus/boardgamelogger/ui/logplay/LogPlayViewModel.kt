package dev.tavarus.boardgamelogger.ui.logplay

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.tavarus.boardgamelogger.data.apimodels.Player
import dev.tavarus.boardgamelogger.data.apimodels.PlayerColor
import dev.tavarus.boardgamelogger.data.plays.PlaysRepository
import dev.tavarus.boardgamelogger.domain.Play
import dev.tavarus.boardgamelogger.domain.PlayerScore
import dev.tavarus.boardgamelogger.domain.Score
import dev.tavarus.boardgamelogger.ui.shared.Action
import dev.tavarus.boardgamelogger.ui.shared.ActionViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LogPlayViewModel @Inject constructor(
    val playsRepository: PlaysRepository,
) : ActionViewModel<VMState, LogPlayAction>(VMState()) {

    fun logPlay() {
        viewModelScope.launch {
            playsRepository.logPlay(
                Play(
                    uiState.value.scores.filterIsInstance<PlayerScoreItem.ActivePlayer>()
                        .map { it.score },
                ),
                uiState.value.game // this needs to be updated
            )
        }

    }
}

sealed class LogPlayAction : Action<VMState> {
    data class GameTitleChange(val title: String) : LogPlayAction()
    data class ScoreFocused(val scoreIndex: Int) : LogPlayAction()

    data class ScoreUpdated(val index: Int, val updatedScore: PlayerScore) : LogPlayAction()

    override fun update(state: VMState) = when (this) {
        is GameTitleChange -> state.copy(game = this.title)
        is ScoreFocused -> {
            var newState = state.copy(selectedPlayer = scoreIndex)
            val focusedScore = state.scores[scoreIndex]
            if (focusedScore is PlayerScoreItem.NewPlayer) {
                var colorIndex =
                    PlayerColor.entries.indexOf(
                        state.scores.filterIsInstance<PlayerScoreItem.ActivePlayer>()
                            .lastOrNull()?.score?.player?.backgroundColor
                    ) + 1
                if (colorIndex == PlayerColor.entries.size) {
                    colorIndex = 0
                }
                val newPlayer = PlayerScoreItem.ActivePlayer(
                    PlayerScore(
                        Player("", PlayerColor.entries[colorIndex]),
                        Score.IntScore(null, false)
                    )
                )
                newState = newState.copy(
                    scores = newState.scores.dropLast(1).plus(newPlayer)
                        .plus(PlayerScoreItem.NewPlayer)
                )
            }
            newState
        }

        is ScoreUpdated -> state.copy(
            scores = state.scores.mapIndexed { i, playerScore ->
                if (i == index && playerScore is PlayerScoreItem.ActivePlayer) {
                    PlayerScoreItem.ActivePlayer(updatedScore)
                } else playerScore
            }
        )
    }
}

data class VMState(
    val game: String = "", // This will eventually be a BoardGame object
    val scores: List<PlayerScoreItem> = listOf(PlayerScoreItem.NewPlayer),
    val selectedPlayer: Int? = null,
)

sealed interface PlayerScoreItem {
    data class ActivePlayer(val score: PlayerScore) : PlayerScoreItem
    data object NewPlayer : PlayerScoreItem
}