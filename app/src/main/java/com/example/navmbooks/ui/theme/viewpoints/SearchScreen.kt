package com.example.navmbooks.ui.theme.viewpoints

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.navmbooks.R
import com.example.navmbooks.database.ContentWithChapterInfo
import com.example.navmbooks.database.DatabaseViewModel
import com.example.navmbooks.ui.theme.Book
import com.example.navmbooks.ui.theme.BookViewModel
import com.example.navmbooks.ui.theme.NavRoutes
import androidx.lifecycle.viewModelScope


/**
 * search words within the chapter contents being shown
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
    navController: NavController,
    modifier: Modifier = Modifier,
    viewModel: BookViewModel,
    dataViewModel: DatabaseViewModel,
    books: List<Book?>,
    padding: PaddingValues
) {
    var expanded by rememberSaveable { mutableStateOf(false) }
    var selectedTitle by rememberSaveable { mutableStateOf("") }
    var searchQuery by remember { mutableStateOf(TextFieldValue("")) }
    val selectedId by viewModel.selectedId.observeAsState(0)
    val searchResults = remember(searchQuery.text) {
        mutableStateOf<List<ContentWithChapterInfo>>(emptyList())
    }

    /*LaunchedEffect(searchQuery.text) {
        if (searchQuery.text.isNotEmpty()) {
            // Search content in the book
            searchResults.value = dataViewModel.searchContentInBook(selectedId, searchQuery.text)
        } else {
            // Reset search results if query is empty
            searchResults.value = emptyList()
        }
    }*/

    val titles = viewModel.titles

    Column(
        modifier = modifier
            .padding(padding)
            .padding(16.dp)
    ) {
        // Dropdown with titles
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded },
            modifier = Modifier
                .fillMaxWidth()
        ) {
            OutlinedTextField(
                readOnly = true,
                value = selectedTitle,
                onValueChange = { },
                label = { Text(text = stringResource(id = R.string.search_book)) }, // Wrap stringResource in Text
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                colors = OutlinedTextFieldDefaults.colors(),
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { expanded = true }
                    .menuAnchor()
            )
            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                books.forEach { book ->
                    DropdownMenuItem(
                        text = { Text(book?.title.orEmpty()) },
                        onClick = {
                            selectedTitle = book?.title.orEmpty() // Update selected title
                            expanded = false
                            viewModel.updateSelectedIdByTitle(dataViewModel, selectedTitle)
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Selected ID: $selectedId",
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(vertical = 8.dp)
        )

        // Search Bar
        OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            label = { Text(text = stringResource(R.string.enter_keyword)) },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {

            },
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.secondaryContainer,
                contentColor = MaterialTheme.colorScheme.primary
            ),
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Text(text = stringResource(R.string.search))
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Results List
        LazyColumn(
            modifier = Modifier.fillMaxWidth()
        ) {
            searchResults.value.forEach { result ->
                item {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                navController.navigate(
                                    NavRoutes.ReadingScreen.createRoute(
                                        result.chapterId,
                                        result.contentId
                                    )
                                )
                            }
                            .padding(vertical = 8.dp)
                    ) {
                        Text(
                            text = result.chapterContent,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            }
        }
    }
}

