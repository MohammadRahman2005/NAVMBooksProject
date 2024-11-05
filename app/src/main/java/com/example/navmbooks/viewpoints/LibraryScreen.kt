package com.example.navmbooks.viewpoints

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.navigation.NavController
import com.example.navmbooks.NavRoutes

@Composable
fun LibraryScreen(
    navController: NavController,
    modifier: Modifier = Modifier,
    padding: PaddingValues
) {
    Column {
        Row(modifier = modifier.padding(padding)){
            Text(text="Library", modifier = Modifier.testTag("LibraryText"))
        }
        Row {
            Column {
                Button(onClick = {navController.navigate(NavRoutes.ContentScreen.route)},
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.secondaryContainer,
                        contentColor = MaterialTheme.colorScheme.primary
                    )
                ) {
                    Text(text="Book 1 table of contents")
                }
            }
        }
    }
}