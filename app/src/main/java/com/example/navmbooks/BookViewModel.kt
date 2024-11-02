package com.example.navmbooks

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

class BookViewModel : ViewModel() {

    var isReadingMode = mutableStateOf(false)
        private set

    fun toggleReadingMode(isEnabled: Boolean) {
        isReadingMode.value = isEnabled
    }
}