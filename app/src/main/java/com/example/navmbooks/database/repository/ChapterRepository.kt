package com.example.navmbooks.database.repository

import com.example.navmbooks.database.dao.ChapterDao
import com.example.navmbooks.database.entities.Chapter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ChapterRepository(private val chapterDao: ChapterDao) {

    // Insert chapters into the database
    suspend fun insertChapters(chapters: Chapter): Int {
        return withContext(Dispatchers.IO) {
            chapterDao.insertChapters(chapters)
        }.toInt()
    }

    // Get all chapters for a specific book
    fun getChaptersByBook(bookId: Int): List<Chapter> {
        return chapterDao.getChaptersByBook(bookId)
    }
}
