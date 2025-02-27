package dev.tavarus.boardgamelogger.ui.logplay

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.navigation.toRoute
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.tavarus.boardgamelogger.data.BoardGameRepository
import javax.inject.Inject

@HiltViewModel
class LogPlayViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    val boardGameRepository: BoardGameRepository
): ViewModel() {
    val gameId = savedStateHandle.toRoute<LogPlay>().gameId
    var boardGame = boardGameRepository.getBoardGame(gameId)
}