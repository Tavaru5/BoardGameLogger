package dev.tavarus.boardgamelogger.ui.logplay

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import dev.tavarus.boardgamelogger.data.RemoteData
import kotlinx.serialization.Serializable

@Serializable
data class LogPlay(val gameId: String)

const val IMAGE_SIZE_OFFSET = "imageSizeOffset"
const val SUBTITLE_OFFSET = "subtitleOffset"
const val HEADER_OFFSET = "headerOffset"

@Composable
fun LogPlayScreen(
    viewModel: LogPlayViewModel
) {
    val game = viewModel.collectUIState()
    val scrollState = rememberScrollState()
    val currentDensity = LocalDensity.current
    var titleHeight by remember { mutableStateOf(0.dp) }
    val connection = remember {
        CollapsingHeaderScrollConnection(
            mapOf(
                IMAGE_SIZE_OFFSET to Pair(0.dp, -(140).dp), // Height of the first image collapse
                HEADER_OFFSET to Pair(0.dp, -(108).dp), // Height of the rest of the image + padding
                SUBTITLE_OFFSET to Pair(0.dp, 0.dp), // Gets set later to the subtitle height
            ), currentDensity
        )
    }

    Box(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.primaryContainer)
            .fillMaxSize()
            .nestedScroll(connection)
    ) {

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(scrollState)
                .padding(
                    top = 100.dp // Height of the image
                            + connection.getTotalOffsets()
                            + titleHeight
                )
        ) {
            for (i in 1..100) {
                Text(
                    modifier = Modifier.padding(20.dp),
                    text = i.toString()
                )
            }
        }

        when (game.value.boardGame) {
            is RemoteData.Failure -> {
                Text("FAILE")
            } // TODO: FAILURE
            RemoteData.Loading -> Text("LODD") // TODO: LOADING
            is RemoteData.Success -> {
                val boardGame = (game.value.boardGame as RemoteData.Success).data
                GameHeader(
                    boardGame,
                    connection.getCurrentValue(IMAGE_SIZE_OFFSET)!!,
                    connection.getCurrentValue(SUBTITLE_OFFSET)!!,
                    connection.getOpacity(SUBTITLE_OFFSET, 0.66f)!!,
                    connection.getCurrentValue(HEADER_OFFSET)!!,
                    connection.getOpacity(HEADER_OFFSET, 0.66f)!!,
                    { measuredSubtitleHeight ->
                        connection.updateConstraint(
                            replaceKey = SUBTITLE_OFFSET,
                            endDp = 0.dp - measuredSubtitleHeight
                        )
                    },
                    { measuredTitleHeight ->
                        if (titleHeight == 0.dp) {
                            titleHeight = measuredTitleHeight
                        }
                    }
                )
            }
        }
    }
}
