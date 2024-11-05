package com.example.navmbooks.viewpoints

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.navmbooks.BookViewModel
import com.example.navmbooks.Chapter

@Composable
fun ReadingScreen(
    navController: NavHostController,
    bookViewModel: BookViewModel,
    modifier: Modifier = Modifier,
    padding: PaddingValues = PaddingValues(0.dp),
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
                Text("Reading Mode", modifier = Modifier.padding(end = 8.dp).testTag("ReadingText"))
                Switch(
                    checked = bookViewModel.isReadingMode.value,
                    onCheckedChange = { bookViewModel.toggleReadingMode(it) },
                    Modifier.testTag("ReadingSwitch")
                )
                }
            Text("Reading Content Here", modifier.testTag("ContentText"))
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Text(text = "Chapter ${Chapter.chapNum}, Title: ${Chapter.title}")
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = Chapter.text)
        }
    }
}
