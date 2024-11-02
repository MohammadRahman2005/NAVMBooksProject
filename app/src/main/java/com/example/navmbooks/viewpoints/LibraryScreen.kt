package com.example.navmbooks.viewpoints

import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavController

@Composable
fun LibraryScreen(navController: NavController) {
    Row(){
        Text(text="Library")
    }
}