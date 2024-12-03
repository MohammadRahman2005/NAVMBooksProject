package com.example.navmbooks.ui.theme.viewpoints

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.navigation.NavController
import com.example.navmbooks.R
import com.example.navmbooks.ui.theme.Book
import com.example.navmbooks.ui.theme.BookViewModel
import com.example.navmbooks.ui.theme.NavRoutes

/**
 * This screen shows the chapters for a chosen book from library
 */
@Composable
fun ContentScreen(
    navController: NavController,
    modifier: Modifier = Modifier,
    viewModel: BookViewModel,
    bookIndex: Int,
    books: List<Book?>
) {
    Image(
        painter = painterResource(R.drawable.app_bg),
        contentDescription = null,
        modifier = Modifier.fillMaxSize(),
        contentScale = ContentScale.Crop
    )
    val book = books[bookIndex]
    val context = LocalContext.current

    // Load the last accessed chapter
    val sharedPreferences = context.getSharedPreferences("book_preferences", Context.MODE_PRIVATE)
    val lastAccessedChapter = sharedPreferences.getInt("last_accessed_chapter_$bookIndex", -1)

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(dimensionResource(R.dimen.medium_padding))
            .verticalScroll(rememberScrollState())
    ) {

        if (book == null) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(text = stringResource(R.string.content_label))
            }
        } else {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = dimensionResource(R.dimen.medium_padding)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = stringResource(R.string.select_chapter),
                    style = MaterialTheme.typography.headlineLarge.copy(fontWeight = FontWeight.Bold)
                )
            }
            Spacer(modifier = Modifier.height(dimensionResource(R.dimen.medium_padding)))

            // Button to resume reading if there's a last accessed chapter
            if (lastAccessedChapter > 0 && lastAccessedChapter <= book.chapters.size) {
                Button(
                    onClick = {
                        navController.navigate(NavRoutes.ReadingScreen.createRoute(bookIndex, lastAccessedChapter - 1))
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = dimensionResource(R.dimen.tiny_padding))
                ) {
                    Text(text = stringResource(R.string.resume_reading))
                }
            }

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