package dev.tavarus.boardgamelogger.ui.logplay

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.tavarus.boardgamelogger.data.RemoteData
import dev.tavarus.boardgamelogger.data.apimodels.Player
import dev.tavarus.boardgamelogger.data.apimodels.PlayerColor
import dev.tavarus.boardgamelogger.data.plays.PlaysRepository
import dev.tavarus.boardgamelogger.domain.Play
import dev.tavarus.boardgamelogger.domain.PlayerScore
import dev.tavarus.boardgamelogger.domain.Score
import dev.tavarus.boardgamelogger.ui.shared.Action
import dev.tavarus.boardgamelogger.ui.shared.ActionViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LogPlayViewModel @Inject constructor(
    val playsRepository: PlaysRepository,
) : ActionViewModel<VMState, LogPlayAction>(VMState()) {
    override val tag = "LogPlay"
    var nameQueryJob: Job? = null

    fun logPlay() {
        dispatch(LogPlayAction.ShowLoading)
        viewModelScope.launch {
            val response = playsRepository.logPlay(
                Play(
                    uiState.value.scoreItems.filterIsInstance<PlayerScoreItem.ActivePlayer>()
                        .map { it.score },
                ),
                uiState.value.game // this needs to be updated
            )
            if (response) {
                dispatch(LogPlayAction.SuccessfulLog)
            } else {
                dispatch(LogPlayAction.ShowError(Error("TODO")))
            }
        }
    }

    fun updateName(index: Int, name: String) {
        dispatch(LogPlayAction.NameUpdated(index, name))
        nameQueryJob?.cancel()
        nameQueryJob = viewModelScope.launch {
            dispatch(
                LogPlayAction.SuggestionsReceived(
                    playsRepository.queryPlayers(name).map { it.name })
            )
        }
    }

    fun updateScore(index: Int, score: String) {
        dispatch(LogPlayAction.ScoreUpdated(index, score))
    }

    fun toggleWinner(index: Int) {
        dispatch(LogPlayAction.WinnerToggled(index))
    }
}

sealed class LogPlayAction : Action<VMState> {
    data class GameTitleChange(val title: String) : LogPlayAction()
    data class ScoreFocused(val scoreIndex: Int) : LogPlayAction()
    data class ScoreUpdated(val index: Int, val scoreText: String) : LogPlayAction()
    data class NameUpdated(val index: Int, val name: String) : LogPlayAction()
    data class WinnerToggled(val index: Int) : LogPlayAction()
    data object ShowLoading : LogPlayAction()
    data object SuccessfulLog : LogPlayAction()
    data class ShowError(val error: Error) : LogPlayAction()
    data class SuggestionsReceived(val suggestions: List<String>) : LogPlayAction()

    override fun update(state: VMState) = when (this) {
        is GameTitleChange -> state.copy(game = this.title)
        is ScoreFocused -> {
            var newState = state.copy(selectedPlayer = scoreIndex, nameSuggestions = listOf())
            val focusedScore = state.scoreItems[scoreIndex]
            if (focusedScore is PlayerScoreItem.NewPlayer) {
                var colorIndex =
                    PlayerColor.entries.indexOf(
                        state.scoreItems.filterIsInstance<PlayerScoreItem.ActivePlayer>()
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
                    scoreItems = newState.scoreItems.dropLast(1).plus(newPlayer)
                        .plus(PlayerScoreItem.NewPlayer)
                )
            }
            newState
        }

        is ScoreUpdated -> state.updateActivePlayerItem(index) {
            it.updateScore(scoreText)
        }

        is NameUpdated -> state.updateActivePlayerItem(index) {
            it.copy(player = it.player.copy(name = name))
        }

        is WinnerToggled -> state.updateActivePlayerItem(index) {
            it.updateWinner(!it.score.winner)
        }

        is ShowLoading -> state.copy(
            successState = RemoteData.Loading
        )

        is ShowError -> state.copy(
            successState = RemoteData.Failure(error)
        )

        is SuccessfulLog -> state.copy(
            successState = RemoteData.Success(true)
        )

        is SuggestionsReceived -> state.copy(
            nameSuggestions = suggestions
        )
    }
}

fun VMState.updateActivePlayerItem(index: Int, scoreUpdate: (PlayerScore) -> PlayerScore): VMState {
    return this.copy(
        scoreItems = scoreItems.mapIndexed { i, scoreItem ->
            if (i == index && scoreItem is PlayerScoreItem.ActivePlayer) {
                PlayerScoreItem.ActivePlayer(scoreUpdate(scoreItem.score))
            } else scoreItem
        }
    )
}

data class VMState(
    val game: String = "", // This will eventually be a BoardGame object
    val scoreItems: List<PlayerScoreItem> = listOf(PlayerScoreItem.NewPlayer),
    val selectedPlayer: Int? = null,
    val successState: RemoteData<Boolean> = RemoteData.Success(false),
    val nameSuggestions: List<String> = listOf(),
)

sealed interface PlayerScoreItem {
    data class ActivePlayer(val score: PlayerScore) : PlayerScoreItem
    data object NewPlayer : PlayerScoreItem
}