package com.example.navmbooks.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.lifecycle.LiveData
import com.example.navmbooks.database.entities.Book

@Dao
interface BookDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBooks(books: Book): Long

    @Query("""
        SELECT 
            b.bookId, b.title, b.imagePath, a.authorName, a.authorId
        FROM 
            books b
        JOIN 
            authors a ON b.authorId = a.authorId
        WHERE 
            b.bookId = :bookId
    """)
    suspend fun getBookMetadata(bookId: Int): Book

    // Get all books
    @Query("SELECT * FROM books")
    fun getAllBooks(): List<Book>

    // Get bookId by title
    @Query("SELECT bookId FROM books WHERE title = :title LIMIT 1")
    suspend fun getBookIdByTitle(title: String): Int?

}
