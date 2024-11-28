package com.example.navmbooks.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.navmbooks.database.dao.AuthorDao
import com.example.navmbooks.database.dao.BookDao
import com.example.navmbooks.database.dao.ChapterDao
import com.example.navmbooks.database.dao.ContentDao
import com.example.navmbooks.database.entities.Author
import com.example.navmbooks.database.entities.Book
import com.example.navmbooks.database.entities.Chapter
import com.example.navmbooks.database.entities.Content

@Database(
    entities = [Author::class, Book::class, Chapter::class, Content::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun authorDao(): AuthorDao
    abstract fun bookDao(): BookDao
    abstract fun chapterDao(): ChapterDao
    abstract fun contentDao(): ContentDao
}
