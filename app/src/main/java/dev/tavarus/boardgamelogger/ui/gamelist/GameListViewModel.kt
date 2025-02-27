package dev.tavarus.boardgamelogger.ui.gamelist

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.tavarus.boardgamelogger.data.GameListRepository
import dev.tavarus.boardgamelogger.data.RemoteData
import dev.tavarus.boardgamelogger.domain.GameList
import dev.tavarus.boardgamelogger.ui.shared.Action
import dev.tavarus.boardgamelogger.ui.shared.ActionViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.mapLatest
import javax.inject.Inject

@HiltViewModel
class GameListViewModel @Inject constructor(
    val gameListRepository: GameListRepository
) : ActionViewModel<VMState, GameListAction>(VMState()) {


    fun onSearchTextChange(text: String) {
        dispatch(GameListAction.SearchTextUpdated(text))
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    fun getGameList(username: String) {
        gameListRepository.getGameList(username).mapLatest { data ->
            dispatch(GameListAction.GameListReceived(username, data))
        }.launchIn(viewModelScope)
    }


}

sealed class GameListAction : Action<VMState> {
    data class SearchTextUpdated(val text: String) : GameListAction()
    data class GameListReceived(val key: String, val gameList: RemoteData<GameList>) :
        GameListAction()

    override fun update(state: VMState) = when (this) {
        is SearchTextUpdated -> state.copy(
            searchText = text
        )

        is GameListReceived -> state.copy(
            sections = state.sections.plus(Pair(key, gameList))
        )
    }
}

data class VMState(
    val sections: Map<String, RemoteData<GameList>> = mapOf(),
    val searchText: String = "",
)