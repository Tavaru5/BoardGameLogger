package dev.tavarus.boardgamelogger.ui.logplay

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuAnchorType
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.unit.dp
import dev.tavarus.boardgamelogger.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun <T> DropdownSearchInput(
    searchSuggestions: List<T>,
    onMenuItemSelected: (T) -> Unit,
    displayName: (T) -> String,
    textInput: @Composable (Modifier.() -> Modifier) -> Unit,
) {
    var hasFocus by remember { mutableStateOf(false) }
    var expanded by remember { mutableStateOf(false) }
    val containerShape = RoundedCornerShape(12.dp)

    ExposedDropdownMenuBox(
        expanded = expanded && searchSuggestions.isNotEmpty(),
        onExpandedChange = { shouldExpand ->
            expanded = shouldExpand
        }
    ) {
        textInput {
            this
                .menuAnchor(
                    ExposedDropdownMenuAnchorType.PrimaryEditable,
                    enabled = true
                )
                .onFocusChanged { focusState ->
                    hasFocus = focusState.isFocused
                    expanded = focusState.isFocused
                }
        }


        ExposedDropdownMenu(
            expanded = expanded && searchSuggestions.isNotEmpty(),
            onDismissRequest = { expanded = false },
            shape = containerShape,
            modifier = Modifier
                .clip(containerShape)
                .background(
                    MaterialTheme.colorScheme.surface,
                    containerShape
                )
                .border(
                    width = 1.dp,
                    color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f),
                    shape = containerShape
                )
        ) {
            /**
             * I'd love to be able to trim the height of this (ie show max 4.5 items), but it
             * doesn't seem like there's a way to do that without remeasuring each time it's
             * updated, which wouldn't be great for search suggestions.
             */
            searchSuggestions.forEach { suggestion ->
                DropdownMenuItem(
                    text = {
                        Text(displayName(suggestion))
                    },
                    onClick = {
                        onMenuItemSelected(suggestion)
                    },
                    modifier = Modifier
                        .padding(horizontal = 8.dp),
                    colors = MenuDefaults.itemColors(
                        textColor = MaterialTheme.colorScheme.onSurface,
                    )
                )
            }
        }
    }
}