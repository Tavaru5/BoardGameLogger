package dev.tavarus.boardgamelogger.ui

import androidx.compose.runtime.Composable
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import dev.tavarus.boardgamelogger.ui.gameinfo.GameInfoRoute
import dev.tavarus.boardgamelogger.ui.gameinfo.GameInfoScreen
import dev.tavarus.boardgamelogger.ui.gameinfo.GameInfoViewModel
import dev.tavarus.boardgamelogger.ui.gamelist.GameList
import dev.tavarus.boardgamelogger.ui.gamelist.GameListScreen
import dev.tavarus.boardgamelogger.ui.gamelist.GameListViewModel
import dev.tavarus.boardgamelogger.ui.logplay.LogPlayScreen
import dev.tavarus.boardgamelogger.ui.logplay.LogPlayScreenRoute
import dev.tavarus.boardgamelogger.ui.logplay.LogPlayViewModel

@Composable
fun NavComponent() {
    val navController = rememberNavController()
    NavHost(navController, startDestination = GameList) {
        composable<GameList> {
            val viewModel = hiltViewModel<GameListViewModel>()
            GameListScreen(
                viewModel = viewModel,
                navigateToGame = { gameId -> navController.navigate(GameInfoRoute(gameId)) },
                navigateToLogPlay = { navController.navigate(LogPlayScreenRoute) }
            )
        }
        composable<GameInfoRoute> { backStackEntry ->
            val viewModel = hiltViewModel<GameInfoViewModel>()
            GameInfoScreen(viewModel)
        }
        composable<LogPlayScreenRoute> {
            val viewModel = hiltViewModel<LogPlayViewModel>()
            LogPlayScreen(viewModel)
        }
    }
}