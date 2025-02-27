package dev.tavarus.boardgamelogger.ui.logplay

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import dev.tavarus.boardgamelogger.R
import dev.tavarus.boardgamelogger.domain.BoardGame
import dev.tavarus.boardgamelogger.ui.CoilPreview
import dev.tavarus.boardgamelogger.ui.ThemedBackground

@Composable
fun GameDetails(
    boardGame: BoardGame
) {
    Column(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp)) {
        Row(
            modifier = Modifier
                .height(240.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            Card(
                shape = RoundedCornerShape(20.dp),
                elevation = CardDefaults.cardElevation(10.dp)
            ) {
                AsyncImage(
                    modifier = Modifier
                        .aspectRatio(1f)
                        .clip(RoundedCornerShape(20.dp))
                        .fillMaxHeight(),
                    model = boardGame.image,
                    contentDescription = stringResource(R.string.cd_bg_thumbnail, boardGame.name),
                    contentScale = ContentScale.Fit
                )
            }
        }
        Text(
            modifier = Modifier.padding(top = 8.dp),
            text = boardGame.name,
            color = MaterialTheme.colorScheme.primary,
            style = MaterialTheme.typography.titleLarge
        )
    }
}

@Preview
@Composable
fun GameDetailsPreviewLight() {
    val boardGame = BoardGame(
        name = "7 Wonders Duel",
        objectId = "",
        image = "",
        thumbnail = "",
    )

    ThemedBackground {
        CoilPreview {
            GameDetails(boardGame)
        }
    }
}

@Preview
@Composable
fun GameDetailsPreviewDark() {
    val boardGame = BoardGame(
        name = "7 Wonders Duel",
        objectId = "",
        image = "",
        thumbnail = "",
    )

    ThemedBackground(darkTheme = true) {
        CoilPreview {
            GameDetails(boardGame)
        }
    }

}


