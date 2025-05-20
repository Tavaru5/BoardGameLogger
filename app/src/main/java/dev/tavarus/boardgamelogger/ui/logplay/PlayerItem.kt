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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.FocusState
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import dev.tavarus.boardgamelogger.R
import dev.tavarus.boardgamelogger.data.apimodels.Player
import dev.tavarus.boardgamelogger.data.apimodels.PlayerColor
import dev.tavarus.boardgamelogger.domain.PlayerScore
import dev.tavarus.boardgamelogger.domain.Score
import dev.tavarus.boardgamelogger.ui.theme.BoardGameLoggerTheme
import dev.tavarus.boardgamelogger.ui.theme.LocalCustomColorsPalette
import dev.tavarus.boardgamelogger.ui.theme.toLocalColor

@Composable
fun PlayerItem(
    modifier: Modifier = Modifier,
    playerScore: PlayerScore,
    onFocused: (FocusState) -> Unit,
    isSelected: Boolean,
    isFocused: Boolean,
    onNameChanged: (String) -> Unit,
    onScoreChanged: (String) -> Unit,
    onWinnerTapped: () -> Unit,
) {
    val iconDrawable: Int
    val iconTint: Color
    val focusRequester = remember { FocusRequester() }
    if (playerScore.score.winner) {
        iconDrawable = R.drawable.crown_filled
        iconTint = MaterialTheme.colorScheme.primary
    } else {
        iconDrawable = R.drawable.crown
        iconTint = MaterialTheme.colorScheme.primary.copy(alpha = 0.38f)
    }
    val border = if (isSelected) {
        BorderStroke(1.dp, MaterialTheme.colorScheme.primary)
    } else null

    LaunchedEffect(isSelected) {
        if (isSelected && !isFocused) {
            focusRequester.requestFocus()
        }
    }

    Card(
        modifier = modifier,
        elevation = CardDefaults.cardElevation(4.dp),
        border = border,
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(6.dp))
                .background(playerScore.player.backgroundColor.toLocalColor(LocalCustomColorsPalette.current))
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            PlayerTextField(
                modifier = Modifier.fillMaxWidth(0.6f).padding(end = 8.dp).focusRequester(focusRequester).onFocusChanged { onFocused(it) },
                value = playerScore.player.name,
                onValueChange = onNameChanged,
                placeHolderText = "Name",
            )
            playerScore.score.formatScore()?.let {
                PlayerTextField(
                    modifier = Modifier.width(56.dp).padding(end = 8.dp).onFocusChanged { onFocused(it) },
                    value = it,
                    onValueChange = onScoreChanged,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    placeHolderText = "0"
                )
            }

            Icon(
                modifier = Modifier.clickable {
                    onWinnerTapped()
                },
                painter = painterResource(iconDrawable),
                tint = iconTint,
                contentDescription = "Crown Icon",
            )

        }
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlayerTextField(
    modifier: Modifier = Modifier,
    value: String,
    onValueChange: (String) -> Unit,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
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
                OutlinedTextFieldDefaults.Container(
                    enabled = true,
                    isError = false,
                    interactionSource = interactionSource,
                )
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
                    playerScore = PlayerScore(Player("Tav", it),Score.IntScore(0, false)),
                    onFocused = {},
                    isSelected = false,
                    isFocused = false,
                    onNameChanged = {},
                    onScoreChanged = {},
                    onWinnerTapped = {}
                )
            }
        }
    }
}
