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
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.navmbooks.BookViewModel
import com.example.navmbooks.R

@Composable
fun ReadingScreen(
    navController: NavHostController,
    bookViewModel: BookViewModel,
    modifier: Modifier = Modifier,
    padding: PaddingValues = PaddingValues(dimensionResource(R.dimen.zero_padding))
) {

    Column(
        modifier = modifier
            .padding(padding)
    ){
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(stringResource(
                R.string.reading_button),
                modifier = Modifier
                    .padding(end = dimensionResource(R.dimen.small_padding))
                    .testTag("ReadingText"))
            Switch(
                checked = bookViewModel.isReadingMode.value,
                onCheckedChange = { bookViewModel.toggleReadingMode(it) },
                Modifier.testTag("ReadingSwitch")
            )
        }
        Text("Reading Content Here", modifier.testTag("ContentText"))
    }
}
