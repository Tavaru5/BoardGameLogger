package dev.tavarus.boardgamelogger.ui.logplay

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.layout
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import dev.tavarus.boardgamelogger.R
import dev.tavarus.boardgamelogger.domain.BoardGame
import dev.tavarus.boardgamelogger.domain.PlayTime
import dev.tavarus.boardgamelogger.domain.PlayerCount
import dev.tavarus.boardgamelogger.ui.CoilPreview
import dev.tavarus.boardgamelogger.ui.shared.formatText
import dev.tavarus.boardgamelogger.ui.theme.BoardGameLoggerTheme

@Composable
fun GameHeader(
    boardGame: BoardGame,
    imageSizeOffset: Dp,
    subtitleOffset: Dp,
    subtitleAlpha: Float,
    headerOffset: Dp,
    imageAlpha: Float,
    onSubtitleMeasured: (Dp) -> Unit,
    onTitleMeasured: (Dp) -> Unit,
) {

    val currentDensity = LocalDensity.current
    Surface(
        modifier = Modifier.offset {
            IntOffset(0, headerOffset.roundToPx())
        },
        shadowElevation = 4.dp,
    ) {

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
                .padding(16.dp)
                .layout { measurable, constraints ->
                    val placeable = measurable.measure(constraints)
                    layout(placeable.width, placeable.height + subtitleOffset.roundToPx()) {
                        // Where the composable gets placed
                        placeable.place(0, 0)
                    }
                },
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Card(
                modifier = Modifier
                    .height(240.dp + imageSizeOffset)
                    .alpha(imageAlpha),
                shape = RoundedCornerShape(20.dp),
                elevation = CardDefaults.cardElevation(10.dp),
            ) {
                AsyncImage(
                    modifier = Modifier
                        .clip(RoundedCornerShape(20.dp))
                        .fillMaxHeight()
                        .aspectRatio(1f),
                    model = boardGame.image,
                    contentDescription = stringResource(
                        R.string.cd_bg_thumbnail,
                        boardGame.name
                    ),
                    contentScale = ContentScale.Fit
                )
            }
            Text(
                modifier = Modifier
                    .padding(top = 8.dp)
                    .onSizeChanged { size ->
                        onTitleMeasured(with(currentDensity) { size.height.toDp() } + 8.dp) // Include padding
                    },
                text = stringResource(R.string.title_format, boardGame.name, boardGame.year),
                color = MaterialTheme.colorScheme.primary,
                style = MaterialTheme.typography.titleLarge,
                textAlign = TextAlign.Center
            )
            Row(
                Modifier
                    .height(IntrinsicSize.Max)
                    .padding(top = 8.dp)
                    .onSizeChanged { size ->
                        onSubtitleMeasured(with(currentDensity) { size.height.toDp() } + 8.dp) // Include padding
                    }
                    .offset { IntOffset(0, subtitleOffset.roundToPx()) }
                    .alpha(subtitleAlpha)) {
                Text(
                    modifier = Modifier.weight(1f),
                    text = boardGame.playTime.formatText(),
                    style = MaterialTheme.typography.titleSmall,
                    textAlign = TextAlign.Center,
                )
                Box(
                    modifier = Modifier
                        .background(MaterialTheme.colorScheme.primary)
                        .fillMaxHeight()
                        .width(2.dp)
                )
                Text(
                    modifier = Modifier.weight(1f),
                    text = boardGame.playerCount.formatText(),
                    style = MaterialTheme.typography.titleSmall,
                    textAlign = TextAlign.Center,
                )
            }
        }

    }
}

@PreviewLightDark
@Composable
fun GameHeaderPreview() {
    val boardGame = BoardGame(
        name = "7 Wonders Duel",
        objectId = "",
        image = "",
        thumbnail = "",
        year = "2018",
        description = "",
        playerCount = PlayerCount(2, 2),
        playTime = PlayTime(0, 30, 0)
    )

    BoardGameLoggerTheme(dynamicColor = false) {
        CoilPreview {
            GameHeader(boardGame, 0.dp, 0.dp, 1f, 0.dp, 1f, {}, {})
        }
    }
}

