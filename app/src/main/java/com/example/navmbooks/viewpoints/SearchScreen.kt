package com.example.navmbooks.viewpoints

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController

@Composable
fun SearchScreen(
    navController: NavController,
    modifier: Modifier = Modifier,
    padding: PaddingValues
) {
    Row(modifier = modifier.padding(padding)){
        Text(text="Search")
    }
}