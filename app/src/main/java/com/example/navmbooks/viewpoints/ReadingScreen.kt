package com.example.navmbooks.viewpoints

import android.graphics.BitmapFactory
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.windowsizeclass.WindowHeightSizeClass.Companion.Compact
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController
import com.example.navmbooks.BookViewModel
import com.example.navmbooks.Chapter
import com.example.navmbooks.R
import com.example.navmbooks.data.ImageItem
import com.example.navmbooks.data.TextItem
import com.example.navmbooks.utils.AdaptiveNavigationType

/**
 * this is the screen which displays the content of a chapter
 */
@Composable
fun ReadingScreen(
    navController: NavHostController,
    bookViewModel: BookViewModel,
    modifier: Modifier = Modifier,
    padding: PaddingValues = PaddingValues(dimensionResource(R.dimen.zero_padding)),
    Chapter: Chapter,
    adaptiveNavType: AdaptiveNavigationType,
    ) {
    val chunkedContent = remember(adaptiveNavType, Chapter.content) {
        when (adaptiveNavType) {
            AdaptiveNavigationType.BOTTOM_NAVIGATION -> Chapter.content.chunked(10)
            AdaptiveNavigationType.NAVIGATION_RAIL -> Chapter.content.chunked(4)
            else -> Chapter.content.chunked(2)
        }
    }

    Column (modifier = modifier
        .padding(padding)
        .fillMaxHeight()
    )
    {
        Column{
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(stringResource(R.string.reading_label), modifier = Modifier.padding(end = dimensionResource(R.dimen.small_padding)).testTag("ReadingText"))
                Switch(
                    checked = bookViewModel.isReadingMode.value,
                    onCheckedChange = { bookViewModel.toggleReadingMode(it) },
                    Modifier.testTag("ReadingSwitch")
                )
            }
            Text(stringResource(R.string.content_label), modifier.testTag("ContentText"))
        }

        LazyRow(
            modifier = Modifier
                .fillMaxHeight()
                .fillMaxWidth()
        ) {
            items(Chapter.content.chunked(10)) { chunk ->
                Column {
                    chunk.forEach { item ->
                        when (item) {
                            is TextItem -> {
                                Text(text = item.text)
                            }
                            is ImageItem -> {
                                val bitmap = remember(item.imagePath) {
                                    BitmapFactory.decodeFile(item.imagePath)?.asImageBitmap()
                                }
                                if (bitmap != null) {
                                    Image(
                                        bitmap = bitmap,
                                        contentDescription = item.imagePath,
                                        modifier = Modifier
                                            .size(dimensionResource(R.dimen.extra_large_size))
                                            .align(Alignment.CenterHorizontally)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

    }
}
