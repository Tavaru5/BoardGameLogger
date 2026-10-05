package dev.tavarus.boardgamelogger.ui.logplay

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import dev.tavarus.boardgamelogger.R
import dev.tavarus.boardgamelogger.data.apimodels.Player
import dev.tavarus.boardgamelogger.data.apimodels.PlayerColor
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
    onFocused: (FocusState) -> Unit,
    isSelected: Boolean,
    onScoreUpdated: ((PlayerScore) -> PlayerScore) -> Unit,
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
            PlayerTextField(
                modifier = Modifier
                    .fillMaxWidth(0.6f)
                    .modifyIf(isNewPlayer) {
                        drawDottedBackground()
                    }
                    .focusRequester(focusRequester)
                    .onFocusChanged { onFocused(it) },
                value = playerScore?.player?.name ?: "",
                onValueChange = { name ->
                    onScoreUpdated { (player, score) ->
                        PlayerScore(player.copy(name = name), score)
                    }
                },
                placeHolderText = if (isNewPlayer) {
                    stringResource(R.string.log_play_new_player_placeholder)
                } else {
                    stringResource(R.string.log_play_name_placeholder)
                },
                keyboardOptions = KeyboardOptions.Default.copy(
                    capitalization = KeyboardCapitalization.Words,
                    imeAction = ImeAction.Next,
                ),
                isNewPlayer = isNewPlayer,
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
                    onScoreUpdated { (player, score) ->
                        PlayerScore(player, score.updateScore(newScore))
                    }
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
                        onScoreUpdated { (player, score) ->
                            PlayerScore(player, score.updateWinner(!score.winner))
                        }
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
    val enabled = true
    val singleLine = true

    BasicTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier,
        interactionSource = interactionSource,
        enabled = enabled,
        singleLine = singleLine,
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
            enabled = enabled,
            singleLine = singleLine,
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

@PreviewLightDark
@Composable
fun PlayerItemPreview() {

    BoardGameLoggerTheme(dynamicColor = false) {
        val colors = PlayerColor.entries
        Column(Modifier.background(color = MaterialTheme.colorScheme.primaryContainer)) {
            colors.forEach {
                PlayerItem(
                    modifier = Modifier.padding(8.dp),
                    playerScoreItem = PlayerScoreItem.ActivePlayer(PlayerScore(Player("Tav", it), Score.IntScore(0, false))),
                    onFocused = {},
                    isSelected = false,
                    onScoreUpdated = {},
                    onNext = {},
                )
            }
            PlayerItem(
                modifier = Modifier.padding(8.dp),
                playerScoreItem = PlayerScoreItem.NewPlayer,
                onFocused = {},
                isSelected = false,
                onScoreUpdated = {},
                onNext = {},
            )
        }
    }
}
