package com.example.navmbooks

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow

class BookViewModel : ViewModel() {

    private var _isReadingMode = false
    val isReadingMode : Boolean get () = _isReadingMode

    fun toggleReadingMode(isEnabled: Boolean) {
        _isReadingMode = isEnabled
    }
}