package com.example.navmbooks.database

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.navmbooks.database.entities.Author
import com.example.navmbooks.database.entities.Book
import com.example.navmbooks.database.entities.Chapter
import com.example.navmbooks.database.entities.Content
import com.example.navmbooks.database.repository.AuthorRepository
import com.example.navmbooks.database.repository.BookRepository
import com.example.navmbooks.database.repository.ChapterRepository
import com.example.navmbooks.database.repository.ContentRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


class DatabaseViewModel(application: Application) : AndroidViewModel(application) {

    private val authorRepository: AuthorRepository
    private val bookRepository: BookRepository
    private val chapterRepository: ChapterRepository
    private val contentRepository: ContentRepository

    init {
        // Initialize the database and DAO instances
        val db = AppDB.getInstance(application)
        val authorDao = db.authorDao()
        val bookDao = db.bookDao()
        val chapterDao = db.chapterDao()
        val contentDao = db.contentDao()

        // Initialize repositories
        authorRepository = AuthorRepository(authorDao)
        bookRepository = BookRepository(bookDao)
        chapterRepository = ChapterRepository(chapterDao)
        contentRepository = ContentRepository(contentDao)
    }

    suspend fun insertAuthor(author: Author): Int {
        return authorRepository.insertAuthor(author)
    }

    suspend fun getAuthorById(authorId: Int): Author
    {
        val author = authorRepository.getAuthorById(authorId)
        return author
    }

    suspend fun insertBooks(book: Book): Int
    {
        return bookRepository.insertBooks(book)
    }

    suspend fun getBookById(bookId: Int): Book
    {
        val book = bookRepository.getBookMetadata(bookId)
        return book
    }

    suspend fun getBookIDByTitle(title: String): Int? {
        val book = bookRepository.getBookIdByTitle(title)
        return book
    }


    suspend fun insertChapters(chapters: Chapter): Int
    {
        return chapterRepository.insertChapters(chapters)
    }

    fun getChaptersByBook(bookId: Int): List<Chapter>
    {
        val chapters = chapterRepository.getChaptersByBook(bookId)
        return chapters
    }

    fun insertContents(content: Content){
        contentRepository.insertContents(content)
    }

    fun getContentByChapter(chapterId: Int): List<Content> {
        val chapterContents = contentRepository.getContentByChapter(chapterId)
        return chapterContents
    }

    suspend fun searchContentInBook(bookId: Int, keyword: String): List<ContentWithChapterInfo> {
        return withContext(Dispatchers.IO) {
            contentRepository.searchContentInBook(bookId, keyword)
        }
    }

    fun getAllBooks(): List<Book>? {
        return bookRepository.getAllBooks()
    }
}