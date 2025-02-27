package dev.tavarus.boardgamelogger.ui.logplay

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import dev.tavarus.boardgamelogger.R
import dev.tavarus.boardgamelogger.data.RemoteData
import dev.tavarus.boardgamelogger.domain.BoardGame
import dev.tavarus.boardgamelogger.ui.gamelist.GameSection
import kotlinx.serialization.Serializable

@Serializable
data class LogPlay(val gameId: String)

@Composable
fun LogPlayScreen(
    viewModel: LogPlayViewModel
) {
    val game = viewModel.boardGame.collectAsState(RemoteData.Loading)
    Column(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.primaryContainer)
            .fillMaxSize()
            .statusBarsPadding()
            .padding(16.dp)
    ) {

        when (game.value) {
            is RemoteData.Failure -> {
                Text("FAILE")
            } // TODO: FAILURE
            RemoteData.Loading -> Text("LODD") // TODO: LOADING
            is RemoteData.Success -> {
                val boardGame = (game.value as RemoteData.Success).data
                GameDetails(boardGame)
            }
        }
    }
}
