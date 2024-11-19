package com.example.navmbooks.viewpoints

import android.graphics.BitmapFactory
import androidx.compose.foundation.Image
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
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

/**
 * This is the library screen where we show the books
 */
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
        .padding(padding)
        .verticalScroll(rememberScrollState()),
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
                    val coverImage = BitmapFactory.decodeFile(book.coverImage.absolutePath)

                    Image(
                        coverImage!!.asImageBitmap(),
                        contentDescription = book.title,
                        modifier = modifier
                            .size(dimensionResource(R.dimen.extra_large_size))
                            .align(Alignment.CenterVertically)
                    )
                    Text(
                        text=book.title
                    )
                }
            }
        }
    }
}