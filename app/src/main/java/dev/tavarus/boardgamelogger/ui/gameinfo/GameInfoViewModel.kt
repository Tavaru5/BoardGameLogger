package dev.tavarus.boardgamelogger.ui.gameinfo

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.tavarus.boardgamelogger.data.RemoteData
import dev.tavarus.boardgamelogger.data.games.BoardGameRepository
import dev.tavarus.boardgamelogger.data.plays.PlaysRepository
import dev.tavarus.boardgamelogger.domain.BoardGame
import dev.tavarus.boardgamelogger.domain.Play
import dev.tavarus.boardgamelogger.ui.shared.Action
import dev.tavarus.boardgamelogger.ui.shared.ActionViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class GameInfoViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    val boardGameRepository: BoardGameRepository,
    val playsRepository: PlaysRepository,
) : ActionViewModel<VMState, GameInfoAction>(VMState()) {
    override val tag = "GameInfo"
    init {
        // Should I consolidate how I do these? Should they all be "remotecall"?
        val gameId = savedStateHandle.toRoute<GameInfoRoute>().gameId
        boardGameRepository.getBoardGame(gameId).mapLatest { boardGame ->
            dispatch(GameInfoAction.BoardGameReceived(boardGame))
        }.launchIn(viewModelScope)
        viewModelScope.launch {
            dispatch(GameInfoAction.PlaysReceived(playsRepository.getPlays(gameId)))
        }
    }

}

sealed class GameInfoAction : Action<VMState> {
    data class BoardGameReceived(val boardGame: RemoteData<BoardGame>) : GameInfoAction()
    data class PlaysReceived(val plays: List<Play>) : GameInfoAction()

    override fun update(state: VMState) = when (this) {
        is BoardGameReceived -> state.copy(boardGame = boardGame)
        is PlaysReceived -> state.copy(previousPlays = plays)
    }
}

data class VMState(
    val boardGame: RemoteData<BoardGame> = RemoteData.Loading,
    val previousPlays: List<Play> = listOf(), //This should maybe be a remotedata, since it will get pulled from the db
)