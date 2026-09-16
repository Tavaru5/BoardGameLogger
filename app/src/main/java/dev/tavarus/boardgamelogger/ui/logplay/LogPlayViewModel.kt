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
                    uiState.value.scores,
                ),
                uiState.value.game // this needs to be updated
            )
        }

    }
}

sealed class LogPlayAction : Action<VMState> {
    data class GameTitleChange(val title: String) : LogPlayAction()
    data class ScoreFocused(val scoreIndex: Int) : LogPlayAction()

    data class NameUpdated(val index: Int, val name: String) : LogPlayAction()
    data class ScoreUpdated(val index: Int, val score: String) : LogPlayAction()
    data class WinnerToggled(val index: Int) : LogPlayAction()
    object NewPlayerCreated : LogPlayAction()

    override fun update(state: VMState) = when (this) {
        is GameTitleChange -> state.copy(game = this.title)
        is ScoreFocused -> state.copy(selectedPlayer = scoreIndex, focusedPlayer = scoreIndex)
        is NameUpdated -> state.copy(
            scores = state.scores.mapIndexed { i, playerScore ->
                if (i == index) {
                    playerScore.copy(player = playerScore.player.copy(name = name))
                } else playerScore
            }
        )

        is ScoreUpdated -> state.copy(
            scores = state.scores.mapIndexed { i, playerScore ->
                if (i == index) {
                    playerScore.copy(score = playerScore.score.updateScore(score))
                } else playerScore
            }
        )

        is WinnerToggled -> state.copy(
            scores = state.scores.mapIndexed { i, playerScore ->
                if (i == index) {
                    playerScore.copy(score = playerScore.score.updateWinner(!playerScore.score.winner))
                } else playerScore
            }
        )

        NewPlayerCreated -> {
            var colorIndex =
                PlayerColor.entries.indexOf(state.scores.lastOrNull()?.player?.backgroundColor) + 1
            if (colorIndex == PlayerColor.entries.size) {
                colorIndex = 0
            }
            val newPlayer = PlayerScore(
                Player("", PlayerColor.entries[colorIndex]),
                Score.IntScore(null, false)
            )
            state.copy(
                scores = state.scores.plus(newPlayer),
                selectedPlayer = state.scores.size
            )
        }
    }
}

data class VMState(
    val game: String = "", // This will eventually be a BoardGame object
    val scores: List<PlayerScore> = listOf(),
    val focusedPlayer: Int? = null,
    val selectedPlayer: Int? = null,
)