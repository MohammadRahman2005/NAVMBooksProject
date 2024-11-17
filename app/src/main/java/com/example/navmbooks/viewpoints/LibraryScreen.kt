package com.example.navmbooks.viewpoints

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.example.navmbooks.Book
import com.example.navmbooks.NavRoutes
import com.example.navmbooks.R

@Composable
fun LibraryScreen(
    navController: NavController,
    modifier: Modifier = Modifier,
    padding: PaddingValues,
    books: List<Book?>
) {
    Column(
        modifier = Modifier
        .fillMaxSize()
        .padding(padding),
        horizontalAlignment = Alignment.CenterHorizontally, // Center horizontally
        verticalArrangement = Arrangement.Center ) {
        Row(modifier = modifier.padding(padding)){
            Text(text=stringResource(R.string.lib_label), modifier = Modifier.testTag("LibraryText"), style = MaterialTheme.typography.headlineMedium)
        }
        Spacer(modifier = Modifier.height(dimensionResource(R.dimen.big_padding)))
        books.forEachIndexed { index, book ->
            Button(onClick = {
                navController.navigate(NavRoutes.ContentScreen.createRoute(index))
            }, colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.secondaryContainer,
                        contentColor = MaterialTheme.colorScheme.primary
                    )) {
                if (book != null) {
                    val coverImage = book.coverImage
                    val painter = rememberAsyncImagePainter(ImageRequest.Builder(LocalContext.current).data(
                            data = coverImage  // Pass the File directly here
                        ).apply(block = fun ImageRequest.Builder.() {
                            crossfade(true)  // Optional: Adds a fade effect when the image loads
                        }).build()
                        )
                    Image(
                        painter = painter,
                        contentDescription = book.title,
                        modifier = modifier
                            .size(150.dp)
                            .align(Alignment.CenterVertically)
                    )
                }
            }
        }
    }
}