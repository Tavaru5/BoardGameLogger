package dev.tavarus.boardgamelogger.ui.logplay

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.toRoute
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.tavarus.boardgamelogger.data.BoardGameRepository
import dev.tavarus.boardgamelogger.data.RemoteData
import dev.tavarus.boardgamelogger.domain.BoardGame
import dev.tavarus.boardgamelogger.domain.Play
import dev.tavarus.boardgamelogger.domain.Player
import dev.tavarus.boardgamelogger.domain.PlayerColor
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
    data class PlayerSelected(val index: Int) : LogPlayAction()
    data class NameUpdated(val index: Int, val name: String) : LogPlayAction()
    data class ScoreUpdated(val index: Int, val score: String) : LogPlayAction()
    data class WinnerToggled(val index: Int) : LogPlayAction()

    override fun update(state: VMState) = when (this) {
        is BoardGameReceived -> state.copy(boardGame = boardGame)
        is PlayerSelected -> state.copy(selectedPlayer = index)
        is NameUpdated -> state.copy(
            currentPlay = Play(state.currentPlay?.scores?.mapIndexed { i, playerScore ->
                if (i == index) {
                    playerScore.copy(player = playerScore.player.copy(name = name))
                } else playerScore
            } ?: listOf())
        )

        is ScoreUpdated -> state.copy(
            currentPlay = Play(state.currentPlay?.scores?.mapIndexed { i, playerScore ->
                if (i == index) {
                    playerScore.copy(score = playerScore.score.updateScore(score))
                } else playerScore
            } ?: listOf())
        )
        is WinnerToggled -> state.copy(
            currentPlay = Play(state.currentPlay?.scores?.mapIndexed { i, playerScore ->
                if (i == index) {
                    playerScore.copy(score = playerScore.score.updateWinner(!playerScore.score.winner))
                } else playerScore
            } ?: listOf())
        )
    }
}

data class VMState(
    val boardGame: RemoteData<BoardGame> = RemoteData.Loading,
    val previousPlays: List<Play> = listOf(), //This should maybe be a remotedata, since it will get pulled from the db
    val currentPlay: Play? = Play(
        listOf(
            PlayerScore(Player("Tav", PlayerColor.TEAL), Score.IntScore(0, true)),
            PlayerScore(Player("Crav", PlayerColor.PINK), Score.IntScore(0, false)),
            PlayerScore(Player("Mav", PlayerColor.PURP), Score.IntScore(0, false)),
            PlayerScore(Player("Dav", PlayerColor.YELLOW), Score.IntScore(0, false)),
        )
    ), // This will be created when the player presses "Log play", just sample data for now
    val selectedPlayer: Int? = null,
)