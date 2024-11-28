package com.example.navmbooks.database.repository

import android.content.Context
import androidx.lifecycle.LiveData
import com.example.navmbooks.database.BookMetadata
import com.example.navmbooks.database.DatabaseProvider
import com.example.navmbooks.database.entities.Book

class BookRepository(context: Context) {
    private val bookDao = DatabaseProvider.getDatabase(context).bookDao()

    // Insert books into the database
    suspend fun insertBooks(books: List<Book>) = bookDao.insertBooks(books)

    // Get book metadata (including author name)
    fun getBookMetadata(bookId: Int): LiveData<BookMetadata> = bookDao.getBookMetadata(bookId)

    // Get all books
    fun getAllBooks(): LiveData<List<Book>> = bookDao.getAllBooks()
}
