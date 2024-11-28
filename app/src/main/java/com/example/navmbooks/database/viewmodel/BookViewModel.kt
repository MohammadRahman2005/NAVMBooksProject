package com.example.navmbooks.database.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.navmbooks.database.BookMetadata
import com.example.navmbooks.database.entities.Book
import com.example.navmbooks.database.repository.BookRepository
import kotlinx.coroutines.launch

class BookViewModel(private val repository: BookRepository) : ViewModel() {

    // LiveData for book metadata (including author information)
    fun getBookMetadata(bookId: Int): LiveData<BookMetadata> = repository.getBookMetadata(bookId)

    // LiveData for all books
    fun getAllBooks(): LiveData<List<Book>> = repository.getAllBooks()

    // Insert books into the database
    fun insertBooks(books: List<Book>) {
        viewModelScope.launch {
            repository.insertBooks(books)
        }
    }
}
