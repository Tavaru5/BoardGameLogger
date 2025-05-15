package dev.tavarus.boardgamelogger.ui.logplay

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import dev.tavarus.boardgamelogger.R
import dev.tavarus.boardgamelogger.data.apimodels.Player
import dev.tavarus.boardgamelogger.data.apimodels.PlayerColor
import dev.tavarus.boardgamelogger.domain.PlayerScore
import dev.tavarus.boardgamelogger.domain.Score
import dev.tavarus.boardgamelogger.ui.theme.BoardGameLoggerTheme

@Composable
fun NewPlayerItem(
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    val iconDrawable: Int = R.drawable.crown
    val iconTint: Color = MaterialTheme.colorScheme.primary.copy(alpha = 0.38f)
    val stroke = Stroke(width = 2f,
        pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f), 0f)
    )


    Card(
        modifier = modifier.background(MaterialTheme.colorScheme.primaryContainer).clickable {
            onClick()
        },
        shape = RoundedCornerShape(10f),
        elevation = CardDefaults.cardElevation(4.dp),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.primaryContainer)
                .padding(1.dp)
                .drawBehind {
                    drawRoundRect(color = Color(0xFF999999), cornerRadius = CornerRadius(x = 10f, y = 10f), style = stroke)
                }
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            NewPlayerTextField(
                modifier = Modifier.fillMaxWidth(0.6f).padding(end = 8.dp),
                text = "New Player",
            )
            NewPlayerTextField(
                modifier = Modifier.width(56.dp).padding(end = 8.dp),
                text = "0",
            )


            Icon(
                painter = painterResource(iconDrawable),
                tint = iconTint,
                contentDescription = "Crown Icon",
            )

        }
    }

}

@Composable
fun NewPlayerTextField(
    modifier: Modifier = Modifier,
    text: String,
) {

    val stroke = Stroke(width = 2f,
        pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f), 0f)
    )

    Text(
        modifier = modifier.drawBehind {
            drawRoundRect(color = Color(0xFF999999), cornerRadius = CornerRadius(x = 10f, y = 10f), style = stroke)
        }.padding(6.dp),
        text = text,
        style = MaterialTheme.typography.bodyLarge.copy(color = Color(0xFF999999))
    )

}

@PreviewLightDark
@Composable
fun NewPlayerItemPreview() {

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
                NewPlayerItem(
                    modifier = Modifier.padding(8.dp),
                ) {}
            }
        }
    }
}
