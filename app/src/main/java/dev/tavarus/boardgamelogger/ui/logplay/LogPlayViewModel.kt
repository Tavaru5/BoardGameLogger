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
import dev.tavarus.boardgamelogger.ui.shared.Action
import dev.tavarus.boardgamelogger.ui.shared.ActionViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.mapLatest
import javax.inject.Inject

@HiltViewModel
class LogPlayViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    val boardGameRepository: BoardGameRepository
): ActionViewModel<VMState, LogPlayAction>(VMState()) {
    init {
        val gameId = savedStateHandle.toRoute<LogPlay>().gameId
        boardGameRepository.getBoardGame(gameId).mapLatest { boardGame ->
            dispatch(LogPlayAction.BoardGameReceived(boardGame))
        }.launchIn(viewModelScope)
    }

}
sealed class LogPlayAction: Action<VMState> {
    data class BoardGameReceived(val boardGame: RemoteData<BoardGame>) : LogPlayAction()

    override fun update(state: VMState) = when (this) {
        is BoardGameReceived -> state.copy(boardGame = boardGame)
    }
}

data class VMState(
    val boardGame: RemoteData<BoardGame> = RemoteData.Loading
)