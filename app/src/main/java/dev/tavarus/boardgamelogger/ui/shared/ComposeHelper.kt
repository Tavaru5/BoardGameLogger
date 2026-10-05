package dev.tavarus.boardgamelogger.ui.shared

import androidx.compose.ui.Modifier

fun Modifier.modifyIf(condition: Boolean, modifier: Modifier.() -> Modifier) = if (condition) {
    this.modifier()
} else {
    this
}
