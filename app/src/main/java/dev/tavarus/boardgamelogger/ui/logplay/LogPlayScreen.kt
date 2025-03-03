package dev.tavarus.boardgamelogger.ui.logplay

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.tavarus.boardgamelogger.data.RemoteData
import kotlinx.serialization.Serializable

@Serializable
data class LogPlay(val gameId: String)

@Composable
fun LogPlayScreen(
    viewModel: LogPlayViewModel
) {
    val game = viewModel.collectUIState()
    val scrollState = rememberScrollState()
    Column(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.primaryContainer)
            .fillMaxSize()
            .verticalScroll(scrollState)
    ) {

        when (game.value.boardGame) {
            is RemoteData.Failure -> {
                Text("FAILE")
            } // TODO: FAILURE
            RemoteData.Loading -> Text("LODD") // TODO: LOADING
            is RemoteData.Success -> {
                val boardGame = (game.value.boardGame as RemoteData.Success).data
                GameHeader(boardGame, scrollState)
            }
        }
    }
}
