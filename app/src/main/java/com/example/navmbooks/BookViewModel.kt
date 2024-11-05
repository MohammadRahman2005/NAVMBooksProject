package com.example.navmbooks

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.IOException

class BookViewModel : ViewModel() {
    var book1 by mutableStateOf<Book?>(null)
        private set
    var book2 by mutableStateOf<Book?>(null)
        private set

    init {
        loadBook()
    }

    private fun loadBook() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                book1 = Book.readBookURL("https://www.gutenberg.org/cache/epub/8710/pg8710-images.html")
                book2 = Book.readBookURL("https://www.gutenberg.org/cache/epub/20195/pg20195-images.html")
                val book3 = Book.readBookURL("https://www.gutenberg.org/cache/epub/40367/pg40367-images.html")
//                book1 = book1
//                book2 = book2
            } catch (e: IOException) {
                // Handle the error appropriately
                e.printStackTrace()
            }
        }
    }

    var isReadingMode = mutableStateOf(false)
        private set

    fun toggleReadingMode(isEnabled: Boolean) {
        isReadingMode.value = isEnabled
    }
}