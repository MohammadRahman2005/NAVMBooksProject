package com.example.navmbooks.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
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
abstract class AppDB : RoomDatabase() {
    abstract fun authorDao(): AuthorDao
    abstract fun bookDao(): BookDao
    abstract fun chapterDao(): ChapterDao
    abstract fun contentDao(): ContentDao

    companion object {

        private var INSTANCE: AppDB? = null
        fun getInstance(context: Context): AppDB {
            synchronized(this) {
                var instance = INSTANCE
                if (instance == null) {
                    instance = Room.databaseBuilder(
                    context.applicationContext,
                        AppDB::class.java,
                    "library_database"
                    ).fallbackToDestructiveMigration()
                    .build()
                    INSTANCE = instance
                }
                return instance
            }
        }
    }
}

