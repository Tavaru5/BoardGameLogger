package dev.tavarus.boardgamelogger.ui.gamelist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.tavarus.boardgamelogger.data.GameListRepository
import dev.tavarus.boardgamelogger.data.RemoteData
import dev.tavarus.boardgamelogger.domain.GameList
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GameListViewModel @Inject constructor(
    val gameListRepository: GameListRepository
): ViewModel() {

    private val _uiState: MutableStateFlow<VMState> = MutableStateFlow(VMState())
    val uiState = _uiState.asStateFlow()

    fun onSearchTextChange(text: String) {
        _uiState.update { state -> state.copy(searchText = text) }
    }

    fun getGameList(username: String) {
        gameListRepository.getGameList(username).mapLatest { data ->
            _uiState.update { state ->
                state.copy(sections = state.sections.plus(Pair(username, data)))
            }
        }.launchIn(viewModelScope)
    }


    // First I can just do it with my userId
}

data class VMState(
    val sections: Map<String, RemoteData<GameList>> = mapOf(),
    val searchText: String = "",
)