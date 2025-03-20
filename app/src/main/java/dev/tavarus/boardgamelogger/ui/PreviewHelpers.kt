package dev.tavarus.boardgamelogger.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import coil3.ColorImage
import coil3.annotation.ExperimentalCoilApi
import coil3.compose.AsyncImagePreviewHandler
import coil3.compose.LocalAsyncImagePreviewHandler
import dev.tavarus.boardgamelogger.ui.theme.BoardGameLoggerTheme
import dev.tavarus.boardgamelogger.ui.theme.DarkBone

@OptIn(ExperimentalCoilApi::class)
@Composable
fun CoilPreview(content: @Composable () -> Unit) {
    val previewHandler = AsyncImagePreviewHandler {
        ColorImage(DarkBone.toArgb())
    }

    CompositionLocalProvider(LocalAsyncImagePreviewHandler provides previewHandler) {
        content()
    }
}

