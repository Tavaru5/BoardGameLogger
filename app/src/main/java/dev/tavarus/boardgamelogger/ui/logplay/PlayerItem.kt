package dev.tavarus.boardgamelogger.ui.logplay

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuAnchorType
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.FocusState
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import dev.tavarus.boardgamelogger.R
import dev.tavarus.boardgamelogger.data.plays.Player
import dev.tavarus.boardgamelogger.data.plays.PlayerColor
import dev.tavarus.boardgamelogger.domain.PlayerScore
import dev.tavarus.boardgamelogger.domain.Score
import dev.tavarus.boardgamelogger.ui.shared.modifyIf
import dev.tavarus.boardgamelogger.ui.theme.BoardGameLoggerTheme
import dev.tavarus.boardgamelogger.ui.theme.LocalCustomColorsPalette
import dev.tavarus.boardgamelogger.ui.theme.toLocalColor

@Composable
fun PlayerItem(
    modifier: Modifier = Modifier,
    playerScoreItem: PlayerScoreItem,
    suggestions: List<String>,
    isSelected: Boolean,
    onFocused: (FocusState) -> Unit,
    onNameUpdated: (String) -> Unit,
    onScoreUpdated: (String) -> Unit,
    onWinnerToggled: () -> Unit,
    onNext: () -> Unit,
) {
    val iconDrawable: Int
    val iconTint: Color
    val focusRequester = remember { FocusRequester() }
    val isNewPlayer = playerScoreItem is PlayerScoreItem.NewPlayer
    val playerScore = (playerScoreItem as? PlayerScoreItem.ActivePlayer)?.score
    if (playerScore?.score?.winner == true) {
        iconDrawable = R.drawable.crown_filled
        iconTint = MaterialTheme.colorScheme.primary
    } else {
        iconDrawable = R.drawable.crown
        iconTint = MaterialTheme.colorScheme.primary.copy(alpha = 0.38f)
    }

    val backgroundColor =
        playerScore?.player?.backgroundColor?.toLocalColor(LocalCustomColorsPalette.current)
            ?: MaterialTheme.colorScheme.primaryContainer
    val border = if (isSelected) {
        BorderStroke(1.dp, MaterialTheme.colorScheme.primary)
    } else null

    Card(
        modifier = modifier,
        shape = RoundedCornerShape(6.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        border = border,
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(6.dp))
                .background(backgroundColor)
                .modifyIf(isNewPlayer) {
                    padding(1.dp).drawDottedBackground()
                }
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            PlayerNameTextField(
                modifier = Modifier
                    .fillMaxWidth(0.6f)

                    .focusRequester(focusRequester)
                    .onFocusChanged { onFocused(it) },
                value = playerScore?.player?.name ?: "",
                onValueChange = { name ->
                    onNameUpdated(name)
                },
                isNewPlayer = isNewPlayer,
                searchSuggestions = suggestions,
                onNext = onNext,
            )
            PlayerTextField(
                modifier = Modifier
                    .width(56.dp)
                    .padding(start = 8.dp)
                    .modifyIf(isNewPlayer) {
                        drawDottedBackground()
                    }
                    .onFocusChanged { onFocused(it) },
                value = playerScore?.score?.formatScore() ?: "",
                onValueChange = { newScore ->
                    onScoreUpdated(newScore)
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Next,
                ),
                isNewPlayer = isNewPlayer,
                onNext = onNext,
                placeHolderText = "0",
            )

            Icon(
                modifier = Modifier
                    .padding(start = 8.dp)
                    .clickable {
                        onWinnerToggled()
                    },
                painter = painterResource(iconDrawable),
                tint = iconTint,
                contentDescription = "Crown Icon",
            )

        }
    }
}

private fun Modifier.drawDottedBackground(): Modifier =
    drawBehind {
        drawRoundRect(
            color = Color(0xFF999999),
            cornerRadius = CornerRadius(x = 10f, y = 10f),
            style = Stroke(
                width = 2f,
                pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f), 0f)
            )
        )
    }


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlayerTextField(
    modifier: Modifier = Modifier,
    value: String,
    onValueChange: (String) -> Unit,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    onNext: () -> Unit,
    isNewPlayer: Boolean,
    placeHolderText: String,
) {
    val interactionSource = remember { MutableInteractionSource() }

    BasicTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier.modifyIf(isNewPlayer) {
            drawDottedBackground()
        },
        interactionSource = interactionSource,
        enabled = true,
        singleLine = true,
        textStyle = MaterialTheme.typography.bodyLarge.copy(color = MaterialTheme.colorScheme.primary),
        keyboardOptions = keyboardOptions,
        keyboardActions = KeyboardActions(
            onNext = { onNext() }
        ),
        cursorBrush = SolidColor(MaterialTheme.colorScheme.primary)
    ) {
        OutlinedTextFieldDefaults.DecorationBox(
            value = value,
            visualTransformation = VisualTransformation.None,
            innerTextField = it,
            interactionSource = interactionSource,
            enabled = true,
            singleLine = true,
            contentPadding = PaddingValues(6.dp),
            placeholder = {
                Text(
                    text = placeHolderText,
                    style = MaterialTheme.typography.bodyLarge
                )
            },
            container = {
                if (!isNewPlayer) {
                    OutlinedTextFieldDefaults.Container(
                        enabled = true,
                        isError = false,
                        interactionSource = interactionSource,
                    )
                }
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlayerNameTextField(
    modifier: Modifier = Modifier,
    value: String,
    onValueChange: (String) -> Unit,
    onNext: () -> Unit,
    searchSuggestions: List<String>,
    isNewPlayer: Boolean,
) {
    var hasFocus by remember { mutableStateOf(false) }
    var expanded by remember { mutableStateOf(false) }
    val placeholderText = if (isNewPlayer) {
        stringResource(R.string.log_play_new_player_placeholder)
    } else {
        stringResource(R.string.log_play_name_placeholder)
    }

    ExposedDropdownMenuBox(
        expanded = expanded && searchSuggestions.isNotEmpty(),
        onExpandedChange = { shouldExpand ->
            expanded = shouldExpand
        }
    ) {
        PlayerTextField(
            modifier = modifier
                .menuAnchor(
                    ExposedDropdownMenuAnchorType.PrimaryEditable,
                    enabled = true
                )
                .onFocusChanged { focusState ->
                    hasFocus = focusState.isFocused
                    expanded = focusState.isFocused
                },
            value = value,
            onValueChange = onValueChange,
            onNext = onNext,
            isNewPlayer = isNewPlayer,
            placeHolderText = placeholderText,
            keyboardOptions = KeyboardOptions.Default.copy(
                capitalization = KeyboardCapitalization.Words,
                imeAction = ImeAction.Next,
            ),
        )
        ExposedDropdownMenu(
            expanded = expanded && searchSuggestions.isNotEmpty(),
            onDismissRequest = { expanded = false },
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier
                .clip(RoundedCornerShape(12.dp))
                .background(
                    MaterialTheme.colorScheme.surface,
                    RoundedCornerShape(12.dp)
                )
                .border(
                    width = 1.dp,
                    color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f),
                    shape = RoundedCornerShape(12.dp)
                )
        ) {
            searchSuggestions.forEach { suggestion ->
                DropdownMenuItem(
                    text = {
                        Text(suggestion)
                    },
                    onClick = {
                        onValueChange(suggestion)
                        onNext()
                    },
                    modifier = Modifier
                        .padding(horizontal = 8.dp, vertical = 2.dp),
                    colors = androidx.compose.material3.MenuDefaults.itemColors(
                        textColor = MaterialTheme.colorScheme.onSurface,
                    )
                )
            }
        }
    }
}

@PreviewLightDark
@Composable
fun PlayerItemPreview() {

    BoardGameLoggerTheme(dynamicColor = false) {
        val colors = PlayerColor.entries
        Column(Modifier.background(color = MaterialTheme.colorScheme.primaryContainer)) {
            colors.forEach {
                PlayerItem(
                    modifier = Modifier.padding(8.dp),
                    playerScoreItem = PlayerScoreItem.ActivePlayer(
                        PlayerScore(
                            Player("Tav", it),
                            Score.IntScore(0, false)
                        )
                    ),
                    isSelected = false,
                    suggestions = listOf(),
                    onFocused = {},
                    onScoreUpdated = {},
                    onNameUpdated = {},
                    onWinnerToggled = {},
                    onNext = {},
                )
            }
            PlayerItem(
                modifier = Modifier.padding(8.dp),
                playerScoreItem = PlayerScoreItem.NewPlayer,
                onFocused = {},
                suggestions = listOf(),
                isSelected = false,
                onScoreUpdated = {},
                onNameUpdated = {},
                onWinnerToggled = {},
                onNext = {},
            )
        }
    }
}
