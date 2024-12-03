package com.example.navmbooks.ui.theme.viewpoints

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
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
import com.example.navmbooks.ui.theme.Book
import com.example.navmbooks.ui.theme.BookViewModel
import com.example.navmbooks.ui.theme.NavRoutes


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

    Image(
        painter = painterResource(R.drawable.app_bg),
        contentDescription = null,
        modifier = Modifier.fillMaxSize(),
        contentScale = ContentScale.Crop
    )

    Column(
        modifier = modifier
            .padding(padding)
            .padding(dimensionResource(R.dimen.medium_padding))
    ) {
        // Dropdown with titles
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded },
            modifier = Modifier
                .fillMaxWidth()
                .testTag("DropdownMenu")
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

        Spacer(modifier = Modifier.height(dimensionResource(R.dimen.medium_padding)))

        // Search Bar
        OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            label = { Text(text = stringResource(R.string.enter_keyword)) },
            modifier = Modifier.fillMaxWidth().testTag("SearchField")
        )

        Spacer(modifier = Modifier.height(dimensionResource(R.dimen.medium_padding)))

        Button(
            onClick = {
                viewModel.performSearch(searchQuery.text)
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.secondaryContainer,
                contentColor = MaterialTheme.colorScheme.primary
            ),
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .testTag("SearchButton")
        ) {
            Text(text = stringResource(R.string.search))
        }

        Spacer(modifier = Modifier.height(dimensionResource(R.dimen.medium_padding)))

        // Results List
        LazyColumn(
            modifier = Modifier.fillMaxWidth()
        ) {
            items(searchResults) { result ->
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("SearchResult")
                        .padding(dimensionResource(R.dimen.small_padding))
                        .clickable {
                            navController.navigate(
                                NavRoutes.ReadingScreen.createRoute(
                                    selectedId - 1,
                                    result.chapterNumber - 1
                                )
                            )
                        }
                        .background(MaterialTheme.colorScheme.surface)
                        .padding(dimensionResource(R.dimen.medium_padding))
                ) {
                    Text(
                        text = stringResource(R.string.click_to_navigate),
                        style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.onSurface, // Dark color
                        modifier = Modifier.padding(bottom = dimensionResource(R.dimen.medium_padding))
                    )
                    val highlightedText = highlightKeyword(result.chapterContent, searchQuery.text)

                    // Display the chapter content
                    Text(
                        text = highlightedText,
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(bottom = dimensionResource(R.dimen.small_padding))
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
                    modifier = Modifier.padding(vertical = dimensionResource(R.dimen.tiny_padding))
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
