package com.example.navmbooks.database.repository

import com.example.navmbooks.database.dao.BookDao
import com.example.navmbooks.database.entities.Book
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class BookRepository(private val bookDao: BookDao) {

    // Insert books into the database
    suspend fun insertBooks(book: Book): Int
    {
        return withContext(Dispatchers.IO) {
            bookDao.insertBooks(book)
        }.toInt()
    }

    // Get book metadata (including author name)
    suspend fun getBookMetadata(bookId: Int): Book
    {
        return bookDao.getBookMetadata(bookId)
    }

    // Get all books
    fun getAllBooks(): List<Book> {
        return bookDao.getAllBooks().distinctBy { it.title }
    }

    // Get bookId by title
    suspend fun getBookIdByTitle(title: String): Int? {
        return withContext(Dispatchers.IO) {
            bookDao.getBookIdByTitle(title)
        }
    }

}
