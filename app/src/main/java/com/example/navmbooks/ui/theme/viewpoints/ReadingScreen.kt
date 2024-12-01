package com.example.navmbooks.ui.theme.viewpoints

import android.graphics.BitmapFactory
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.navmbooks.R
import com.example.navmbooks.data.ImageItem
import com.example.navmbooks.data.TableItem
import com.example.navmbooks.data.TextItem
import com.example.navmbooks.ui.theme.BookViewModel
import com.example.navmbooks.ui.theme.Chapter
import com.example.navmbooks.ui.theme.utils.AdaptiveNavigationType

/**
 * this is the screen which displays the content of a chapter
 */
@Composable
fun ReadingScreen(
    navController: NavHostController,
    bookViewModel: BookViewModel,
    modifier: Modifier = Modifier,
    padding: PaddingValues = PaddingValues(dimensionResource(R.dimen.zero_padding)),
    chapter: Chapter,
    adaptiveNavType: AdaptiveNavigationType,
    ) {
    val configuration = LocalConfiguration.current

    val screenWidth = configuration.screenWidthDp.dp
    val chunkedContent = remember(adaptiveNavType, chapter.content) {
        when (adaptiveNavType) {
            AdaptiveNavigationType.BOTTOM_NAVIGATION -> chapter.content.chunked(3)
            AdaptiveNavigationType.NAVIGATION_RAIL -> chapter.content.chunked(2)
            else -> chapter.content.chunked(1)
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
        }

        LazyRow(
            modifier = Modifier
                .fillMaxHeight()
                .fillMaxWidth(),
        ) {
            items(chunkedContent) { chunk ->
                Column (
                    modifier = Modifier
                        .widthIn(min = screenWidth, max=screenWidth)
                        .padding(horizontal = dimensionResource(R.dimen.medium_padding))
                ){
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
                            is TableItem ->
                                Text(text = item.text)
                        }
                    }
                }
            }
        }

    }
}
