package com.example.navmbooks.viewpoints

import android.graphics.BitmapFactory
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
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
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.navmbooks.BookViewModel
import com.example.navmbooks.Chapter
import com.example.navmbooks.R
import com.example.navmbooks.data.ImageItem
import com.example.navmbooks.data.TextItem

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
) {
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

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(dimensionResource(R.dimen.medium_padding))
        ) {
            Text(
                text = stringResource(R.string.reading_header, Chapter.chapNum, Chapter.title)
            )
            Spacer(modifier = Modifier.height(dimensionResource(R.dimen.small_padding)))
            for (e in Chapter.content){
                if (e is TextItem){
                    Text(
                        text = e.text
                    )
                }else if (e is ImageItem){
                    val bitmap = BitmapFactory.decodeFile(e.imagePath)
                    Log.d("imagePath", e.imagePath)
                    Image(
                        bitmap!!.asImageBitmap(),
                        contentDescription = e.imagePath,
                        modifier = modifier
                            .size(dimensionResource(R.dimen.extra_large_size))
                            .align(Alignment.CenterHorizontally)
                    )
                }
            }
        }
    }
}
