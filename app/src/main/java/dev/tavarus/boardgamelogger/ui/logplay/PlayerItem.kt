package dev.tavarus.boardgamelogger.ui.logplay

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import dev.tavarus.boardgamelogger.R
import dev.tavarus.boardgamelogger.domain.Player
import dev.tavarus.boardgamelogger.ui.theme.BoardGameLoggerTheme
import dev.tavarus.boardgamelogger.ui.theme.LocalCustomColorsPalette

@Composable
fun PlayerItem(
    modifier: Modifier = Modifier,
    player: Player
) {
    var nameField by remember { mutableStateOf("") }
    var scoreField by remember { mutableStateOf("") }
    var winner by remember { mutableStateOf(false) }
    val iconDrawable: Int
    val iconTint: Color
    if (winner) {
        iconDrawable = R.drawable.crown_filled
        iconTint = MaterialTheme.colorScheme.primary
    } else {
        iconDrawable = R.drawable.crown
        iconTint = MaterialTheme.colorScheme.primary.copy(alpha = 0.38f)
    }

    Card(
        modifier = modifier,
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(6.dp))
                .background(player.backgroundColor)
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            PlayerTextField(
                modifier = Modifier.weight(1f).padding(end = 8.dp),
                value = nameField,
                onValueChange = {nameField = it},
                placeHolderText = "Name",
            )
            PlayerTextField(
                modifier = Modifier.width(IntrinsicSize.Min).padding(end = 8.dp),
                value = scoreField,
                onValueChange = {scoreField = it},
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                placeHolderText = "00"
            )
            Icon(
                modifier = Modifier.clickable {
                    winner = !winner
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
        textStyle = MaterialTheme.typography.bodyMedium.copy(color = MaterialTheme.colorScheme.primary),
        keyboardOptions = keyboardOptions,
    ) {
        OutlinedTextFieldDefaults.DecorationBox(
            value = value,
            visualTransformation = VisualTransformation.None,
            innerTextField = it,
            interactionSource = interactionSource,
            enabled = enabled,
            singleLine = singleLine,
            contentPadding = PaddingValues(4.dp),
            placeholder = {
                Text(
                    placeHolderText,
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
        val colors = with(LocalCustomColorsPalette.current) {
            listOf(pink, purp, teal, yellow)
        }
        Column(Modifier.background(color = MaterialTheme.colorScheme.primaryContainer)) {
            colors.forEach {
                PlayerItem(
                    modifier = Modifier.padding(8.dp),
                    player = Player("Tav", it)
                )
            }
        }
    }
}
