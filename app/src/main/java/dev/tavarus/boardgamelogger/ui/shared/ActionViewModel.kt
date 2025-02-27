package dev.tavarus.boardgamelogger.ui.shared

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update

open class ActionViewModel<T, A: Action<T>>(
    initialState: T,
) : ViewModel() {
    private val uiState: MutableStateFlow<T> = MutableStateFlow(initialState)

    fun dispatch(action: A) {
        uiState.update { state ->
            action.update(state)
        }
    }

    @Composable
    fun collectUIState() = uiState.collectAsState()
}

interface Action<T> {
    fun update(state: T): T
}