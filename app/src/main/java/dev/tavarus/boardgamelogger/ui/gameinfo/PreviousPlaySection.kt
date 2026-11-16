package dev.tavarus.boardgamelogger.ui.gameinfo

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import dev.tavarus.boardgamelogger.R
import dev.tavarus.boardgamelogger.data.plays.Player
import dev.tavarus.boardgamelogger.domain.Play
import dev.tavarus.boardgamelogger.domain.PlayerScore
import dev.tavarus.boardgamelogger.domain.Score
import dev.tavarus.boardgamelogger.ui.shared.formatDate
import dev.tavarus.boardgamelogger.ui.theme.BoardGameLoggerTheme

@Composable
fun PreviousPlaySection(
    previousPlays: List<Play>,
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(stringResource(R.string.previous_plays))
        previousPlays.forEach { play ->
            PreviousPlayItem(play)
        }
    }

}

@Composable
fun PreviousPlayItem(
    play: Play,
) {
    // Winner name (in bold? Or brighter at least) with crown & score, other player name + score up to a point where it trails off
    // And then date?
    // Make them expandable; when expanded it shows each player & score
    val winnerMap = play.scores.groupBy { it.score.winner }
    val date =
        Card(
            shape = RoundedCornerShape(12.dp),
            elevation = CardDefaults.cardElevation(4.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(MaterialTheme.colorScheme.secondaryContainer)
                    .padding(8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Row(modifier = Modifier.weight(1f)) {
                    winnerMap[true]?.forEach { winScore ->
                        val scoreTitle = winScore.player.name.plus(
                            winScore.score.formatScore()?.let { formattedScore ->
                                " - $formattedScore"
                            } ?: ""
                        )
                        Text(
                            scoreTitle,
                            fontWeight = FontWeight.Bold,
                        )
                        Icon(
                            modifier = Modifier
                                .padding(horizontal = 4.dp),
                            painter = painterResource(R.drawable.crown_filled),
                            contentDescription = stringResource(R.string.cd_crown_icon),
                        )
                    }
                    winnerMap[false]?.let { NameListEllipsisText(it.map { score -> score.player.name }) }
                }
                Text(
                    modifier = Modifier.padding(8.dp),
                    text = play.formatDate()
                )
            }
        }
}

@PreviewLightDark
@Composable
fun PreviewPreviousPlaySection() {
    BoardGameLoggerTheme {
        PreviousPlaySection(
            listOf(
                Play(
                    scores = listOf(
                        PlayerScore(Player("Tav"), Score.IntScore(11, true)),
                        PlayerScore(Player("Crav"), Score.IntScore(8, false)),
                        PlayerScore(Player("Mav"), Score.IntScore(3, false))
                    ),
                    timestamp = 1793473810204,
                    gameId = ""
                ),
                Play(
                    scores = listOf(
                        PlayerScore(Player("Tav"), Score.IntScore(11, true)),
                        PlayerScore(
                            Player("Super long name"),
                            Score.IntScore(8, false)
                        ),
                        PlayerScore(Player("Mav"), Score.IntScore(3, false))
                    ),
                    timestamp = 1793473810204,
                    gameId = ""
                ),
                Play(
                    scores = listOf(
                        PlayerScore(Player("Tav"), Score.IntScore(11, true)),
                        PlayerScore(Player("Too"), Score.IntScore(8, false)),
                        PlayerScore(Player("Many"), Score.IntScore(7, false)),
                        PlayerScore(Player("Players"), Score.IntScore(6, false)),
                        PlayerScore(Player("To"), Score.IntScore(5, false)),
                        PlayerScore(Player("Count"), Score.IntScore(4, false)),
                        PlayerScore(Player("It"), Score.IntScore(3, false)),
                        PlayerScore(Player("Fills"), Score.IntScore(2, false)),
                    ),
                    timestamp = 1793473810204,
                    gameId = ""
                )
            )
        )
    }
}