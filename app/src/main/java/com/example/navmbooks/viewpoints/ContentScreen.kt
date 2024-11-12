package com.example.navmbooks.viewpoints

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.navmbooks.Book
import com.example.navmbooks.BookViewModel
import com.example.navmbooks.NavRoutes

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

    if (book == null){
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text(text = "Loading book...")
        }
    }else{
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Text(text = "Select a Chapter")
            Spacer(modifier = Modifier.height(16.dp))

            // Assuming Book has a list of chapters
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
                        .padding(vertical = 4.dp)
                ) {
                    Text(text = "Chapter ${index + 1}")
                }
            }
        }
    }
}