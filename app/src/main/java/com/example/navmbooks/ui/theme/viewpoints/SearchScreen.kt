package com.example.navmbooks.ui.theme.viewpoints

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.TabRowDefaults.Divider
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextDecoration
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
    books: List<Book?>,
    padding: PaddingValues
) {
    var expanded by rememberSaveable { mutableStateOf(false) }
    var selectedTitle by rememberSaveable { mutableStateOf("") }
    var searchQuery by remember { mutableStateOf(TextFieldValue("")) }
    val selectedId by viewModel.selectedId.observeAsState(0)
    val searchResults by viewModel.searchResults.observeAsState(emptyList())

    Column(
        modifier = modifier
            .padding(padding)
            .padding(16.dp)
    ) {
        // Dropdown with titles
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded },
            modifier = Modifier.fillMaxWidth()
        ) {
            OutlinedTextField(
                readOnly = true,
                value = selectedTitle,
                onValueChange = { },
                label = { Text(text = stringResource(id = R.string.search_book)) },
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
                            selectedTitle = book?.title.orEmpty()
                            expanded = false
                            viewModel.updateSelectedIdByTitle(selectedTitle)
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
                viewModel.performSearch(searchQuery.text)
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
            items(searchResults) { result ->
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp) // Add padding around each result
                        .clickable {
                            navController.navigate(
                                NavRoutes.ReadingScreen.createRoute(
                                    selectedId,
                                    result.chapterId,
                                )
                            )
                        }
                        .background(MaterialTheme.colorScheme.surface)
                        .padding(16.dp)
                ) {
                    val highlightedText = highlightKeyword(result.chapterContent, searchQuery.text)

                    // Display the chapter content
                    Text(
                        text = highlightedText,
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    // Add metadata or additional info if needed
                    Text(
                        text = "Chapter: ${result.chapterId}",
                        style = MaterialTheme.typography.bodySmall.copy(fontWeight = androidx.compose.ui.text.font.FontWeight.Bold),
                        color = MaterialTheme.colorScheme.onSurface // Dark color
                    )

                }

                Divider(
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.2f),
                    thickness = 1.dp,
                    modifier = Modifier.padding(vertical = 4.dp)
                )
            }
        }

    }
}

fun highlightKeyword(text: String, keyword: String): AnnotatedString {
    if (keyword.isEmpty()) return AnnotatedString(text)

    val annotatedString = buildAnnotatedString {
        var currentIndex = 0
        while (currentIndex < text.length) {
            val keywordIndex = text.indexOf(keyword, currentIndex, ignoreCase = true)
            if (keywordIndex == -1) {
                append(text.substring(currentIndex))
                break
            }
            append(text.substring(currentIndex, keywordIndex))
            pushStyle(
                SpanStyle(
                    color = Color.Red,
                    fontWeight = FontWeight.Bold,
                    textDecoration = TextDecoration.Underline
                )
            )
            append(text.substring(keywordIndex, keywordIndex + keyword.length))
            pop()
            currentIndex = keywordIndex + keyword.length
        }
    }
    return annotatedString
}
