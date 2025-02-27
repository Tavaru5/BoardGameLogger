package dev.tavarus.boardgamelogger.ui.gamelist

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsTopHeight
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.tavarus.boardgamelogger.data.RemoteData
import kotlinx.serialization.Serializable

@Serializable
object GameList

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GameListScreen(
    viewModel: GameListViewModel,
    navigateToGame: (String) -> Unit
) {
    val uiState = viewModel.uiState.collectAsState()
    LaunchedEffect(Unit) {
        viewModel.getGameList("tavarus")
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {

        Surface(
            modifier = Modifier.fillMaxWidth(),
            shadowElevation = 8.dp,
            color = MaterialTheme.colorScheme.primaryContainer
        ) {
            Column(modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)) {

                SearchBar(
                    inputField = {
                        SearchBarDefaults.InputField(
                            query = uiState.value.searchText,
                            onSearch = { },
                            expanded = false,
                            onExpandedChange = {},
                            placeholder = { Text("Search for a game") },
                            onQueryChange = viewModel::onSearchTextChange
                        )
                    },
                    expanded = false,
                    onExpandedChange = {},
                    shadowElevation = 8.dp
                ) {

                }
            }

        }


        uiState.value.sections.forEach { (_, gameSection) ->
            when (gameSection) {
                is RemoteData.Failure -> {
                    Text("FAILE")
                } // TODO: FAILURE
                RemoteData.Loading -> Text("LODD") // TODO: LOADING
                is RemoteData.Success -> {
                    GameSection(
                        gameSection.data,
                        navigateToGame
                    )
                }
            }
        }


        // This needs to be somehow modified for whent he animations happen? So they might need to be pulled up to here unfortunately.
        Spacer(
            modifier = Modifier
                .height(8.dp)
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.tertiaryContainer)
        )
    }


}


