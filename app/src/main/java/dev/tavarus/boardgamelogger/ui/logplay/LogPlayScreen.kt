package dev.tavarus.boardgamelogger.ui.logplay

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.tavarus.boardgamelogger.R
import dev.tavarus.boardgamelogger.ui.gameinfo.GameInfoAction
import dev.tavarus.boardgamelogger.ui.gameinfo.NewPlayerItem
import dev.tavarus.boardgamelogger.ui.gameinfo.PlayerItem
import kotlinx.serialization.Serializable

@Serializable
data object LogPlayScreenRoute

@Composable
fun LogPlayScreen(
    viewModel: LogPlayViewModel,
) {
    val uiState = viewModel.collectUIState()
    Scaffold() { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp)
        ) {
            // Title and go back/close
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = uiState.value.game,
                onValueChange = { viewModel.dispatch(LogPlayAction.GameTitleChange(it)) },
                label = { Text(stringResource(R.string.log_play_board_game_label)) }
            )

            // Section title
            // Do we want a section background???

            uiState.value.scores.forEachIndexed { index, playerScore ->
                PlayerItem(
                    modifier = Modifier.padding(vertical = 8.dp),
                    playerScore = playerScore,
                    onFocused = { focusState ->
                        if (focusState.isFocused) {
                            viewModel.dispatch(LogPlayAction.ScoreFocused(index))
                        }
                    },
                    isSelected = uiState.value.selectedPlayer == index,
                    isFocused = uiState.value.focusedPlayer == index,
                    onNameChanged = { viewModel.dispatch(LogPlayAction.NameUpdated(index, it)) },
                    onScoreChanged = { viewModel.dispatch(LogPlayAction.ScoreUpdated(index, it)) },
                    onWinnerTapped = { viewModel.dispatch(LogPlayAction.WinnerToggled(index)) },
                )
            }
            NewPlayerItem(
                modifier = Modifier.padding(vertical = 8.dp),
            ) {
                viewModel.dispatch(LogPlayAction.NewPlayerCreated)
                // Idk if it should immediately have a color then switch once a player has been added that already has a color?
                // Or if the color shouldn't persist per player
                // I still need to focus the player
                // Focus player
            }

            Button(
                onClick = {
                    viewModel.logPlay()
                },
                enabled = uiState.value.scores.isNotEmpty()
            ) {
                Text("Log Play")
            }
        }
    }

}