package com.example.navmbooks.viewpoints

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import com.example.navmbooks.R

@Composable
fun HomeScreen(
    navController: NavController,
    modifier: Modifier = Modifier,
    padding: PaddingValues
) {
    Row(modifier = modifier.padding(padding)){
        Text(text=stringResource(R.string.home_screen), modifier = Modifier.testTag("homeText"))
    }
}