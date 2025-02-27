package dev.tavarus.boardgamelogger.ui

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import dev.tavarus.boardgamelogger.ui.logplay.LogPlay
import dev.tavarus.boardgamelogger.ui.logplay.LogPlayScreen
import dev.tavarus.boardgamelogger.ui.gamelist.GameList
import dev.tavarus.boardgamelogger.ui.gamelist.GameListScreen
import dev.tavarus.boardgamelogger.ui.gamelist.GameListViewModel
import dev.tavarus.boardgamelogger.ui.logplay.LogPlayViewModel

@Composable
fun NavComponent() {
    val navController = rememberNavController()
    NavHost(navController, startDestination = GameList) {
        composable<GameList> {
            val viewModel = hiltViewModel<GameListViewModel>()
            GameListScreen(viewModel) { gameId -> navController.navigate(LogPlay(gameId)) }
        }
        composable<LogPlay> { backStackEntry ->
            val viewModel = hiltViewModel<LogPlayViewModel>()
            LogPlayScreen(viewModel)
        }
    }
}