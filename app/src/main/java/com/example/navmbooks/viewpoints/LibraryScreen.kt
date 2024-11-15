package com.example.navmbooks.viewpoints

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import com.example.navmbooks.Book
import com.example.navmbooks.BookViewModel
import com.example.navmbooks.NavRoutes
import com.example.navmbooks.R

@Composable
fun LibraryScreen(
    navController: NavController,
    modifier: Modifier = Modifier,
    padding: PaddingValues,
    viewModel: BookViewModel,
    books: List<Book?>
) {
    Column {
        Row(modifier = modifier.padding(padding)){
            Text(text=stringResource(R.string.library_screen), modifier = Modifier.testTag("LibraryText"))
        }
        books.forEachIndexed { index, book ->
            Button(onClick = {
                navController.navigate(NavRoutes.ContentScreen.createRoute(index))
            }, colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.secondaryContainer,
                        contentColor = MaterialTheme.colorScheme.primary
                    )) {
                if (book != null) {
                    Text(text = book.title)
                }
            }
        }
    }
}