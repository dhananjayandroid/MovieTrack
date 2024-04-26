package com.djay.movietrack.ui.screens.widgets

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp

/**
 * SearchAppBar provides a dynamic app bar that toggles between a standard view and a search input view.
 * It allows users to initiate and conclude searches, managing the visibility of the search bar and keyboard interactions.
 *
 * @param searchText The current text in the search field, stored in a MutableState for reactivity.
 * @param showSearchBar A MutableState flag that determines whether the search bar or title should be shown.
 * @param onSearchChanged Function called when the search text is updated.
 * @param onSearchClosed Function called to close the search bar and revert to the normal app bar.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchAppBar(
    searchText: MutableState<String>,
    showSearchBar: MutableState<Boolean>,
    onSearchChanged: (String) -> Unit,
    onSearchClosed: () -> Unit
) {
    val keyboardController =
        LocalSoftwareKeyboardController.current  // Controls the on-screen keyboard.
    val focusManager = LocalFocusManager.current  // Manages focus within composables.
    val focusRequester = remember { FocusRequester() }  // Requests focus for input components.

    if (showSearchBar.value) {
        // AppBar that appears when search is active.
        TopAppBar(
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.primary,
                titleContentColor = MaterialTheme.colorScheme.onPrimary,
                navigationIconContentColor = MaterialTheme.colorScheme.onPrimary,
                actionIconContentColor = MaterialTheme.colorScheme.onSecondary
            ),
            modifier = Modifier
                .padding(10.dp)
                .shadow(
                    elevation = 5.dp,
                    spotColor = Color.DarkGray,
                    shape = RoundedCornerShape(10.dp)
                ),
            title = {
                TextField(
                    value = searchText.value,
                    onValueChange = onSearchChanged,
                    modifier = Modifier
                        .fillMaxWidth()
                        .focusRequester(focusRequester),
                    placeholder = { Text("Search movies...") },
                    colors = TextFieldDefaults.colors(
                        cursorColor = Color.Magenta,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        disabledIndicatorColor = Color.Transparent
                    ),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
                    keyboardActions = KeyboardActions(onDone = {
                        focusManager.clearFocus() // Dismisses the keyboard when search is completed.
                    }),
                )
            },
            navigationIcon = {
                IconButton(onClick = onSearchClosed) {
                    Icon(Icons.Filled.Close, contentDescription = "Close search")
                }
            }
        )
    } else {
        // Default AppBar that displays when search is not active.
        TopAppBar(
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.primary,
                titleContentColor = MaterialTheme.colorScheme.onPrimary,
                navigationIconContentColor = MaterialTheme.colorScheme.onPrimary,
                actionIconContentColor = MaterialTheme.colorScheme.onSecondary
            ),
            modifier = Modifier
                .padding(10.dp)
                .shadow(
                    elevation = 5.dp,
                    spotColor = Color.DarkGray,
                    shape = RoundedCornerShape(10.dp)
                )
                .clickable {
                    showSearchBar.value = true  // Activates the search bar.
                    keyboardController?.show()  // Optionally shows the keyboard immediately.
                },
            title = { Text("Movies") },
            actions = {
                Icon(
                    Icons.Filled.Search, contentDescription = "Search", modifier = Modifier
                        .padding(10.dp)
                )
            }
        )
    }
}
