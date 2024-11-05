package com.example.navmbooks.viewpoints

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.focusModifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.navmbooks.BookViewModel
import com.example.navmbooks.MainScreen

@Composable
fun ReadingScreen(
    navController: NavHostController,
    bookViewModel: BookViewModel,
    modifier: Modifier = Modifier,
    padding: PaddingValues = PaddingValues(0.dp),
) {

    Column(
        modifier = modifier
            .padding(padding)
    ){
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
        MainScreen()
    }
}
