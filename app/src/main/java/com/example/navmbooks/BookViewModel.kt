package com.example.navmbooks

import android.annotation.SuppressLint
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.IOException

@SuppressLint("MutableCollectionMutableState")
class BookViewModel : ViewModel() {
    var book1 by mutableStateOf<Book?>(null)
        private set

    var bookList by mutableStateOf<List<Book?>>(emptyList())
        private set

    var book2 by mutableStateOf<Book?>(null)
        private set

    var book3 by mutableStateOf<Book?>(null)
        private set
    init {
        loadBook()
    }

    private fun loadBook() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                book1 = Book.readBookURL("https://www.gutenberg.org/cache/epub/8710/pg8710-images.html")
                book2 = Book.readBookURL("https://www.gutenberg.org/cache/epub/20195/pg20195-images.html")
                book3 = Book.readBookURL("https://www.gutenberg.org/cache/epub/40367/pg40367-images.html")
                bookList = bookList + book1
                bookList = bookList + book2
                bookList = bookList + book3
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