package dev.tavarus.boardgamelogger.ui.logplay

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.unit.dp
import dev.tavarus.boardgamelogger.R
import kotlinx.serialization.Serializable

@Serializable
data object LogPlayScreenRoute

/**
 * Eventual pieces to be added:
 * Searching for existing games
 * Searching for existing players
 * Different scoring types
 * Enforcing different winner types (ie, 1 winner, multiple winner, no winner etc)
 * Time selector (defaults to now, but could be used for retroactive logging. Maybe just a date picker instead?)
 */
@Composable
fun LogPlayScreen(
    viewModel: LogPlayViewModel,
    onClose: () -> Unit,
) {
    val uiState = viewModel.collectUIState()
    Scaffold(
        containerColor = MaterialTheme.colorScheme.surface
    ) { paddingValues ->
        BackHandler {
            onClose()
        }
        Column {
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shadowElevation = 6.dp,
                color = MaterialTheme.colorScheme.surface,
            ) {
                Row(
                    modifier = Modifier
                        .padding(top = paddingValues.calculateTopPadding())
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        modifier = Modifier
                            .clickable(onClickLabel = stringResource(R.string.click_label_close_button)) {
                                onClose()
                            },
                        imageVector = Icons.Default.Close,
                        contentDescription = stringResource(R.string.cd_close_icon)
                    )
                    Spacer(Modifier.width(24.dp))
                    Text(
                        text = stringResource(R.string.log_play_screen_title),
                        style = MaterialTheme.typography.titleLarge
                    )
                }
            }
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(bottom = paddingValues.calculateBottomPadding())
                    .padding(horizontal = 16.dp)
                    .imePadding()
            ) {


                Spacer(modifier = Modifier.height(32.dp))
                Text("Game")
                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = uiState.value.game,
                    onValueChange = { viewModel.dispatch(LogPlayAction.GameTitleChange(it)) },
                    placeholder = { Text(stringResource(R.string.log_play_board_game_hint)) },
                    keyboardOptions = KeyboardOptions.Default.copy(capitalization = KeyboardCapitalization.Words)
                )

                Spacer(Modifier.height(16.dp))
                Text(stringResource(R.string.log_play_scores_title))
                val focusManager = LocalFocusManager.current
                uiState.value.scoreItems.forEachIndexed { index, playerScore ->
                    val isSelected = uiState.value.selectedPlayer == index
                    PlayerItem(
                        modifier = Modifier.padding(vertical = 4.dp),
                        playerScoreItem = playerScore,
                        isSelected = isSelected,
                        suggestions = if (isSelected) {
                            uiState.value.nameSuggestions
                        } else {
                            listOf()
                        },
                        onFocused = { focusState ->
                            if (focusState.isFocused) {
                                viewModel.dispatch(LogPlayAction.ScoreFocused(index))
                            }
                        },
                        onScoreUpdated = { score ->
                            viewModel.updateScore(index, score)
                        },
                        onNameUpdated = { name ->
                            viewModel.updateName(index, name)
                        },
                        onWinnerToggled = {
                            viewModel.toggleWinner(index)
                        },
                        onNext = { focusManager.moveFocus(FocusDirection.Next) }
                    )
                }


                Spacer(Modifier.height(16.dp))
                Button(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = {
                        viewModel.logPlay()
                    },
                    colors = ButtonDefaults.buttonColors(),
                    enabled = uiState.value.scoreItems.filterIsInstance<PlayerScoreItem.ActivePlayer>()
                        .isNotEmpty()
                ) {
                    Text(
                        text = "Log Play",
                    )
                }
            }
        }
    }
}