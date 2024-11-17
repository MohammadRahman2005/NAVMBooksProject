package com.example.navmbooks.viewpoints

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import com.example.navmbooks.Book
import com.example.navmbooks.BookViewModel
import com.example.navmbooks.NavRoutes
import com.example.navmbooks.R

@Composable
fun ContentScreen(
    navController: NavController,
    modifier: Modifier = Modifier,
    padding: PaddingValues,
    viewModel: BookViewModel,
    bookIndex: Int,
    books: List<Book?>
){
    val book = books[bookIndex]

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(dimensionResource(R.dimen.medium_padding))
            .verticalScroll(rememberScrollState())
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                stringResource(R.string.reading_label),
                modifier = Modifier.padding(end = dimensionResource(R.dimen.small_padding))
            )
            Switch(
                checked = viewModel.isReadingMode.value,
                onCheckedChange = { viewModel.toggleReadingMode(it) },
                modifier = Modifier.padding(end = dimensionResource(R.dimen.small_padding))
            )
        }

        if (book == null){
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(text = stringResource(R.string.content_label))
            }
        } else {
                Text(text = stringResource(R.string.select_chapter))
                Spacer(modifier = Modifier.height(dimensionResource(R.dimen.medium_padding)))

                book.chapters.forEachIndexed { index, chapter ->
                    Button(
                        onClick = {
                            navController.navigate(NavRoutes.ReadingScreen.createRoute(bookIndex, index))
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.secondaryContainer,
                            contentColor = MaterialTheme.colorScheme.primary
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = dimensionResource(R.dimen.tiny_padding))
                    ) {
                        Text(text = stringResource(R.string.chapter_label, index + 1))
                    }
                }
        }
    }
}