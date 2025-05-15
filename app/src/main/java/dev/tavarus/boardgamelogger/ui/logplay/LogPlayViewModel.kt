package dev.tavarus.boardgamelogger.ui.logplay

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.tavarus.boardgamelogger.data.BoardGameRepository
import dev.tavarus.boardgamelogger.data.RemoteData
import dev.tavarus.boardgamelogger.domain.BoardGame
import dev.tavarus.boardgamelogger.domain.Play
import dev.tavarus.boardgamelogger.data.apimodels.Player
import dev.tavarus.boardgamelogger.data.apimodels.PlayerColor
import dev.tavarus.boardgamelogger.domain.PlayerScore
import dev.tavarus.boardgamelogger.domain.Score
import dev.tavarus.boardgamelogger.ui.shared.Action
import dev.tavarus.boardgamelogger.ui.shared.ActionViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.mapLatest
import javax.inject.Inject

@HiltViewModel
class LogPlayViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    val boardGameRepository: BoardGameRepository
) : ActionViewModel<VMState, LogPlayAction>(VMState()) {
    init {
        val gameId = savedStateHandle.toRoute<LogPlay>().gameId
        boardGameRepository.getBoardGame(gameId).mapLatest { boardGame ->
            dispatch(LogPlayAction.BoardGameReceived(boardGame))
        }.launchIn(viewModelScope)
    }

}

sealed class LogPlayAction : Action<VMState> {
    data class BoardGameReceived(val boardGame: RemoteData<BoardGame>) : LogPlayAction()
    data class PlayerFocused(val index: Int) : LogPlayAction()
    data class NameUpdated(val index: Int, val name: String) : LogPlayAction()
    data class ScoreUpdated(val index: Int, val score: String) : LogPlayAction()
    data class WinnerToggled(val index: Int) : LogPlayAction()
    object NewPlayerCreated : LogPlayAction()

    override fun update(state: VMState) = when (this) {
        is BoardGameReceived -> state.copy(boardGame = boardGame)
        is PlayerFocused -> state.copy(selectedPlayer = index, focusedPlayer = index)
        is NameUpdated -> state.copy(
            currentPlay = Play(state.currentPlay?.scores?.mapIndexed { i, playerScore ->
                if (i == index) {
                    playerScore.copy(player = playerScore.player.copy(name = name))
                } else playerScore
            } ?: listOf()))

        is ScoreUpdated -> state.copy(
            currentPlay = Play(state.currentPlay?.scores?.mapIndexed { i, playerScore ->
                if (i == index) {
                    playerScore.copy(score = playerScore.score.updateScore(score))
                } else playerScore
            } ?: listOf()))

        is WinnerToggled -> state.copy(
            currentPlay = Play(state.currentPlay?.scores?.mapIndexed { i, playerScore ->
                if (i == index) {
                    playerScore.copy(score = playerScore.score.updateWinner(!playerScore.score.winner))
                } else playerScore
            } ?: listOf())
        )

        NewPlayerCreated -> {
            var colorIndex = PlayerColor.entries.indexOf(state.currentPlay?.scores?.last()?.player?.backgroundColor) + 1
            if (colorIndex == PlayerColor.entries.size) { colorIndex = 0 }
            val newPlayer = PlayerScore(Player("", PlayerColor.entries[colorIndex]), Score.IntScore(null, false))
            state.copy(currentPlay = Play((state.currentPlay?.scores ?: listOf()).plus(listOf(newPlayer))), selectedPlayer = state.currentPlay?.scores?.size)
        }
    }
}

data class VMState(
    val boardGame: RemoteData<BoardGame> = RemoteData.Loading,
    val previousPlays: List<Play> = listOf(), //This should maybe be a remotedata, since it will get pulled from the db
    val currentPlay: Play? = Play(
        listOf(
            PlayerScore(Player("Tav", PlayerColor.TEAL), Score.IntScore(0, true)),
            PlayerScore(Player("Crav", PlayerColor.PINK), Score.IntScore(0, false)),
//            PlayerScore(Player("Mav", PlayerColor.PURP), Score.IntScore(0, false)),
//            PlayerScore(Player("Dav", PlayerColor.YELLOW), Score.IntScore(0, false)),
        )), // This will be created when the player presses "Log play", just sample data for now
    val selectedPlayer: Int? = null,
    val focusedPlayer: Int? = null,
)