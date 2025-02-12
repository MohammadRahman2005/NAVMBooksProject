package com.example.navmbooks.ui.theme.viewpoints

import android.graphics.BitmapFactory
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.navmbooks.R
import com.example.navmbooks.ui.theme.Book
import com.example.navmbooks.ui.theme.BookViewModel
import com.example.navmbooks.ui.theme.NavRoutes

/**
 * This is the library screen where we show the books
 */
@Composable
fun LibraryScreen(
    navController: NavController,
    modifier: Modifier = Modifier,
    padding: PaddingValues,
    books: List<Book?>,
    viewModel: BookViewModel,
    onResetLastAccessed: () -> Unit
) {
    val titles = viewModel.titles
    val urls = viewModel.urls
    val files = viewModel.files
    val images = viewModel.images
    var loadingProgress = viewModel.loadingTimes

    Image(
        painter = painterResource(R.drawable.app_bg),
        contentDescription = null,
        modifier = Modifier.fillMaxSize(),
        contentScale = ContentScale.Crop
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .testTag("Scroll")
            .padding(padding)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        if (books.isEmpty()) {
            Text(
                text = stringResource(R.string.loading_book),
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.testTag("LoadingText")
            )
        } else {
            Row(modifier = modifier.padding(padding)) {
                Text(
                    text = stringResource(R.string.lib_label),
                    modifier = Modifier.testTag("LibraryText"),
                    style = MaterialTheme.typography.headlineMedium
                )
            }
            Spacer(modifier = Modifier.height(dimensionResource(R.dimen.big_padding)))
            Button(
                onClick = { onResetLastAccessed() },
                modifier = Modifier
                    .padding(vertical = dimensionResource(R.dimen.small_padding))
                    .fillMaxWidth(0.8f)
            ) {
                Text(text = stringResource(R.string.reset_reading))
            }
            Spacer(modifier = Modifier.height(dimensionResource(R.dimen.big_padding)))
            books.forEachIndexed { index, book ->
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .padding(dimensionResource(R.dimen.medium_padding))
                        .testTag("BookItem")
                ) {
                    if (book != null) {
                        val coverImage = BitmapFactory.decodeFile(book.coverImage)

                        Image(
                            coverImage!!.asImageBitmap(),
                            contentDescription = book.title,
                            modifier = Modifier
                                .size(dimensionResource(R.dimen.extra_large_size))
                                .align(Alignment.CenterHorizontally)
                                .clickable {
                                    navController.navigate(NavRoutes.ContentScreen.createRoute(index))
                                }
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = book.title.orEmpty(),
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier.testTag("BookTitle")
                        )
                    }
                }
            }
            titles.forEachIndexed { index, title ->
                val url = urls.getOrNull(index)
                val filePath = files.getOrNull(index)
                val imagePath = images.getOrNull(index)
                val bookExists = viewModel.bookList.any { it?.title?.uppercase()  == title.uppercase()}
                if (!bookExists) {
                    Column {
                        Button(
                            onClick = {
                                if (loadingProgress[index]==0){
                                    if (url != null && filePath != null && imagePath != null) {
                                        viewModel.addBookToBookList(title, url, filePath, imagePath, index)
                                    } else {
                                        Log.e("LibraryScreen", "Invalid data at index $index: url=$url, filePath=$filePath, imagePath=$imagePath")
                                    }
                                }
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.secondaryContainer,
                                contentColor = MaterialTheme.colorScheme.primary
                            ),
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("DownloadButton")
                                .padding(vertical = dimensionResource(R.dimen.tiny_padding))
                        ) {
                            if(loadingProgress[index]==0){
                                Text(text = stringResource(R.string.download_title, title))
                            }else if (loadingProgress[index]==1){
                                Text(text= stringResource(R.string.downloading))
                            }else if(loadingProgress[index]==2){
                                Text (text = stringResource(R.string.parsing))
                            }
                        }

                    }
                }
            }
        }
    }
}