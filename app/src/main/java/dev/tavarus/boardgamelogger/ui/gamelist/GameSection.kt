package dev.tavarus.boardgamelogger.ui.gamelist

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import dev.tavarus.boardgamelogger.R
import dev.tavarus.boardgamelogger.domain.GameList
import dev.tavarus.boardgamelogger.domain.ListGame
import dev.tavarus.boardgamelogger.domain.ListType

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun GameSection(
    gameList: GameList,
    navigateToGame: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(true) }
    val rotationAngle by animateFloatAsState(targetValue = if (expanded) 0f else -90f)
    val title = when (gameList.type) {
        is ListType.PersonalCollection -> stringResource(
            R.string.personal_collection_title,
            gameList.type.username
        )

        ListType.PreviouslyPlayed -> stringResource(R.string.previously_played)
    }

    Column {
        Row(
            Modifier
                .padding(16.dp)
                .height(IntrinsicSize.Max)
                .clickable {
                    expanded = !expanded
                }) {
            Text(
                text = title,
                color = MaterialTheme.colorScheme.primary,
                style = MaterialTheme.typography.titleLarge,
            )
            Icon(
                modifier = Modifier
                    .fillMaxHeight()
                    .rotate(rotationAngle),
                imageVector = Icons.Filled.KeyboardArrowDown,
                tint = MaterialTheme.colorScheme.primary,
                contentDescription = null,
            )
        }
        SharedTransitionLayout {
            AnimatedVisibility(
                visible = expanded,
                enter = EnterTransition.None,
                exit = ExitTransition.None,
            ) {
                LazyVerticalGrid(
                    modifier = Modifier
                        .sharedBounds(
                            rememberSharedContentState(key = "bounds"),
                            animatedVisibilityScope = this@AnimatedVisibility,
                            resizeMode = SharedTransitionScope.ResizeMode.RemeasureToBounds,
                            placeHolderSize = SharedTransitionScope.PlaceHolderSize.animatedSize
                        )
                        .padding(16.dp),
                    columns = GridCells.Fixed(2),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    items(gameList.games) { game ->
                        GameElement(
                            boardGame = game,
                            animatedVisibilityScope = this@AnimatedVisibility,
                            sharedTransitionScope = this@SharedTransitionLayout,
                            navigateToGame = navigateToGame
                        )
                    }
                }

            }
            AnimatedVisibility(
                visible = !expanded,
                enter = EnterTransition.None,
                exit = ExitTransition.None,
            ) {
                LazyRow(
                    modifier = Modifier
                        .sharedBounds(
                            rememberSharedContentState(key = "bounds"),
                            animatedVisibilityScope = this@AnimatedVisibility,
                            resizeMode = SharedTransitionScope.ResizeMode.RemeasureToBounds,
                            placeHolderSize = SharedTransitionScope.PlaceHolderSize.animatedSize
                        )
                        .height(200.dp)
                        .padding(vertical = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    item {
                        Spacer(modifier = Modifier.width(8.dp))
                    }
                    items(gameList.games) { game ->
                        CompactGameElement(
                            boardGame = game,
                            animatedVisibilityScope = this@AnimatedVisibility,
                            sharedTransitionScope = this@SharedTransitionLayout
                        )
                    }
                }

            }

        }
    }


}

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun GameElement(
    boardGame: ListGame,
    sharedTransitionScope: SharedTransitionScope,
    animatedVisibilityScope: AnimatedVisibilityScope,
    navigateToGame: (String) -> Unit,
) {
    with(sharedTransitionScope) {
        Column(modifier = Modifier
            .sharedElement(
                rememberSharedContentState(key = boardGame.name),
                animatedVisibilityScope
            )
            .clickable { navigateToGame(boardGame.objectId) }) {
            Card(
                modifier = Modifier,
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer),
                shape = RoundedCornerShape(20.dp),
                elevation = CardDefaults.cardElevation(8.dp)
            ) {
                AsyncImage(
                    modifier = Modifier
                        .aspectRatio(1f)
                        .clip(RoundedCornerShape(20.dp)),
                    model = boardGame.image,
                    contentDescription = stringResource(R.string.cd_bg_thumbnail, boardGame.name),
                    contentScale = ContentScale.Fit
                )
            }

            Text(
                modifier = Modifier.padding(top = 4.dp),
                text = boardGame.name,
                color = MaterialTheme.colorScheme.primary,
            )
        }
    }
}

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun CompactGameElement(
    boardGame: ListGame,
    sharedTransitionScope: SharedTransitionScope,
    animatedVisibilityScope: AnimatedVisibilityScope
) {
    with(sharedTransitionScope) {
        Column(
            Modifier
                .sharedElement(
                    rememberSharedContentState(key = boardGame.name),
                    animatedVisibilityScope
                )
                .width(IntrinsicSize.Min)
        ) {
            Card(
                modifier = Modifier.weight(1f),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer),
                shape = RoundedCornerShape(20.dp),
                elevation = CardDefaults.cardElevation(8.dp)
            ) {
                AsyncImage(
                    modifier = Modifier
                        .aspectRatio(1f)
                        .clip(RoundedCornerShape(20.dp)),
                    model = boardGame.image,
                    contentDescription = "Thumbnail for ${boardGame.name}",
                    contentScale = ContentScale.Fit
                )
            }
            Text(
                modifier = Modifier
                    .padding(top = 4.dp)
                    .fillMaxWidth(),
                text = boardGame.name,
                color = MaterialTheme.colorScheme.primary,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }

    }
}
