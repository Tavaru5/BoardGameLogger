package dev.tavarus.boardgamelogger

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsBottomHeight
import androidx.compose.foundation.layout.windowInsetsTopHeight
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import dagger.hilt.android.AndroidEntryPoint
import dev.tavarus.boardgamelogger.ui.AppNavHost
import dev.tavarus.boardgamelogger.ui.theme.BoardGameLoggerTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.auto(
                0x22000000, 0x22000000
            )
        )
        setContent {
            BoardGameLoggerTheme {
                Box(
                    modifier = Modifier
                        .background(MaterialTheme.colorScheme.primaryContainer)
                        .fillMaxSize()
                ) {

                    AppNavHost()
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .windowInsetsTopHeight(WindowInsets.systemBars)
                            .background(
                                Color(0x22000000)
                            )
                    )

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .windowInsetsBottomHeight(WindowInsets.systemBars)
                            .background(
                                Color(0x22000000)
                            )
                            .align(Alignment.BottomCenter)
                    )
                }
            }
        }
    }
}
