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
    var chunkedContent: List<List<Any>>? = null
    if (adaptiveNavType == AdaptiveNavigationType.BOTTOM_NAVIGATION){
        chunkedContent = Chapter.content.chunked(6)
    }else if (adaptiveNavType == AdaptiveNavigationType.NAVIGATION_RAIL){
        chunkedContent = Chapter.content.chunked(4)
    }else {
        chunkedContent = Chapter.content.chunked(2)
    }
    var currentPage by remember { mutableStateOf(0) }
    val scrollState = rememberLazyListState()
    Column (modifier = modifier
        .padding(padding)
        .verticalScroll(rememberScrollState()))
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

        LazyRow (modifier = Modifier.fillMaxHeight()
                    .fillMaxWidth(),){
            items(Chapter.content) {item ->
                if (item is TextItem){
                    Text(
                        text = item.text
                    )
                }else if (item is ImageItem){
                    val bitmap = BitmapFactory.decodeFile(item.imagePath)
                    Log.d("imagePath", item.imagePath)
                    Image(
                        bitmap!!.asImageBitmap(),
                        contentDescription = item.imagePath,
                        modifier = modifier
                            .size(dimensionResource(R.dimen.extra_large_size))
                            .align(Alignment.CenterHorizontally)
                    )
                }
            }
        }

//        Column(
//            modifier = Modifier
//                .fillMaxSize()
//                .padding(dimensionResource(R.dimen.medium_padding))
//        ) {
//            Text(
//                text = stringResource(R.string.reading_header, Chapter.chapNum, Chapter.title)
//            )
//            Spacer(modifier = Modifier.height(dimensionResource(R.dimen.small_padding)))
//            for (e in Chapter.content){
//                if (e is TextItem){
//                    Text(
//                        text = e.text
//                    )
//                }else if (e is ImageItem){
//                    val bitmap = BitmapFactory.decodeFile(e.imagePath)
//                    Log.d("imagePath", e.imagePath)
//                    Image(
//                        bitmap!!.asImageBitmap(),
//                        contentDescription = e.imagePath,
//                        modifier = modifier
//                            .size(dimensionResource(R.dimen.extra_large_size))
//                            .align(Alignment.CenterHorizontally)
//                    )
//                }
//            }
//        }
    }
}
