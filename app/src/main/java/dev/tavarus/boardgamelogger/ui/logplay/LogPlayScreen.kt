package dev.tavarus.boardgamelogger.ui.logplay

import android.util.Log
import androidx.compose.foundation.background
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
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import dev.tavarus.boardgamelogger.data.RemoteData
import kotlinx.serialization.Serializable

@Serializable
data class LogPlay(val gameId: String)

const val IMAGE_SIZE = "currentImageSize"
const val SUBTITLE_OFFSET = "subtitleOffset"
const val HEADER_OFFSET = "headerOffset"

@Composable
fun LogPlayScreen(
    viewModel: LogPlayViewModel
) {
    val game = viewModel.collectUIState()
    val scrollState = rememberScrollState()
    val currentDensity = LocalDensity.current
    val connection = remember {
        CollapsingHeaderScrollConnection(
            mapOf(
                IMAGE_SIZE to Pair(240.dp, 100.dp),
                SUBTITLE_OFFSET to Pair(0.dp, 0.dp),
                HEADER_OFFSET to Pair(0.dp, -(100).dp)
            ), currentDensity
        )
    }

    Column(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.primaryContainer)
            .fillMaxSize()
            .nestedScroll(connection)
    ) {

        when (game.value.boardGame) {
            is RemoteData.Failure -> {
                Text("FAILE")
            } // TODO: FAILURE
            RemoteData.Loading -> Text("LODD") // TODO: LOADING
            is RemoteData.Success -> {
                val boardGame = (game.value.boardGame as RemoteData.Success).data
                Log.d("KOG", "re-compose")
                GameHeader(
                    boardGame,
                    connection.getCurrentValue(IMAGE_SIZE)!!,
                    connection.getCurrentValue(SUBTITLE_OFFSET)!!,
                    connection.getOpacity(
                        SUBTITLE_OFFSET,
                        0.66f
                    )!!, // This should make it disappear at 66% height
                    connection.getCurrentValue(HEADER_OFFSET)!!,
                    connection.getOpacity(HEADER_OFFSET, 0.66f)!!,
                    { height ->

                        connection.updateConstraint(
                            replaceKey = SUBTITLE_OFFSET,
                            endDp = 0.dp - (with(currentDensity) { height.toDp() } + 8.dp)
                        )// I hate this but idk how else to get the padding correct
                    }
                )
            }
        }
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(scrollState)
                .offset {
                    IntOffset(
                        0,
                        connection
                            .getCurrentValue(HEADER_OFFSET)!!
                            .roundToPx()
                    )
                }
        ) {
            for (i in 1..100) {
                Text(
                    modifier = Modifier.padding(20.dp),
                    text = i.toString()
                )
            }
        }
    }
}
