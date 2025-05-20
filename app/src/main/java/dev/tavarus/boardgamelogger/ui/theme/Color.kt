package dev.tavarus.boardgamelogger.ui.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import dev.tavarus.boardgamelogger.data.apimodels.PlayerColor

val Purple80 = Color(0xFFD0BCFF)
val PurpleGrey80 = Color(0xFFCCC2DC)

val Purple40 = Color(0xFF6650a4)
val PurpleGrey40 = Color(0xFF625b71)

val BoneWhite = Color(0xFFE6E3DA)
val OceanGrey = Color(0xFFDADDE6)
val BerryPurple = Color(0xFFebd6f1)
val SeafoamGreen = Color(0xFFc5e2ea)
val CoralPink = Color(0xFFEFB8C8)
val LeafYellow = Color(0xFFD3C78F)

val DarkerOcean = Color(0xFF374145)
val DarkOcean = Color(0xFF455459)
val DarkBone = Color(0xFF594A45)
val DarkBerry = Color(0xFF69596f)
val DarkSeafoam = Color(0xFF48636a)
val DarkCoral = Color(0xFF7D5260)
val DarkLeaf = Color(0xFF675f30)


@Immutable
data class CustomColorsPalette(
    val purp: Color = Color.Unspecified,
    val pink: Color = Color.Unspecified,
    val teal: Color = Color.Unspecified,
    val yellow: Color = Color.Unspecified,
)

val LightCustomColorsPalette = CustomColorsPalette(
    purp = BerryPurple,
    pink = CoralPink,
    teal = SeafoamGreen,
    yellow = LeafYellow,
)

val DarkCustomColorsPalette = CustomColorsPalette(
    purp = DarkBerry,
    pink = DarkCoral,
    teal = DarkSeafoam,
    yellow = DarkLeaf,
)

val LocalCustomColorsPalette = staticCompositionLocalOf { CustomColorsPalette() }

@Composable
fun PlayerColor.toLocalColor(colorsPalette: CustomColorsPalette) = when (this) {
    PlayerColor.PURP -> colorsPalette.purp
    PlayerColor.TEAL -> colorsPalette.teal
    PlayerColor.PINK -> colorsPalette.pink
    PlayerColor.YELLOW -> colorsPalette.yellow
}

