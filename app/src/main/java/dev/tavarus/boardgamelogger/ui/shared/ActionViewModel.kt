package dev.tavarus.boardgamelogger.ui.shared

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update

abstract class ActionViewModel<T, A: Action<T>>(
    initialState: T,
) : ViewModel() {
    val uiState: MutableStateFlow<T> = MutableStateFlow(initialState)
    abstract val tag: String
    open val debugMode = false

    fun dispatch(action: A) {
        if (debugMode) {
            Log.d(tag, action.toString())
        }
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