package com.example.navmbooks.database.repository

import androidx.lifecycle.LiveData
import com.example.navmbooks.database.dao.BookDao
import com.example.navmbooks.database.entities.Book
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class BookRepository(private val bookDao: BookDao) {
    private val coroutineScope = CoroutineScope(Dispatchers.Main)

    // Insert books into the database
    fun insertBooks(books: List<Book>)
    {
        coroutineScope.launch(Dispatchers.IO) {
            bookDao.insertBooks(books)
        }
    }

    // Get book metadata (including author name)
    suspend fun getBookMetadata(bookId: Int): Book
    {
        return bookDao.getBookMetadata(bookId)
    }

    // Get all books
    fun getAllBooks(): List<Book>? {
        var books: List<Book>? = null
        coroutineScope.launch(Dispatchers.IO) {
            books = bookDao.getAllBooks()
        }
        return books
    }
}
