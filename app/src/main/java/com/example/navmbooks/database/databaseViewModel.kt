package com.example.navmbooks.database

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.example.navmbooks.database.entities.Author
import com.example.navmbooks.database.entities.Chapter
import com.example.navmbooks.database.entities.Content
import com.example.navmbooks.database.entities.Book
import com.example.navmbooks.database.repository.AuthorRepository
import com.example.navmbooks.database.repository.BookRepository
import com.example.navmbooks.database.repository.ChapterRepository
import com.example.navmbooks.database.repository.ContentRepository


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

    fun insertAuthor(author: Author) {
        authorRepository.insertAuthor(author)
    }

    suspend fun getAuthorById(authorId: Int): Author
    {
        val author = authorRepository.getAuthorById(authorId)
        return author
    }

    fun insertBooks(books: List<Book>){
        bookRepository.insertBooks(books)
    }

    suspend fun getBookById(bookId: Int): Book {
        val book = bookRepository.getBookMetadata(bookId)
        return book
    }

    fun insertChapters(chapters: List<Chapter>)
    {
        chapterRepository.insertChapters(chapters)
    }

    fun getChaptersByBook(bookId: Int): List<Chapter>
    {
        val chapters = chapterRepository.getChaptersByBook(bookId)
        return chapters
    }

    fun insertContents(contents: List<Content>){
        contentRepository.insertContents(contents)
    }

    fun getContentByChapter(chapterId: Int): List<Content> {
        val chapterContents = contentRepository.getContentByChapter(chapterId)
        return chapterContents
    }

    fun searchContentInBook(bookId: Int, keyword: String): List<ContentWithChapterInfo> {
        return contentRepository.searchContentInBook(bookId, keyword)

    }
}