package com.example.navmbooks.database.repository

import androidx.lifecycle.LiveData
import com.example.navmbooks.database.dao.ChapterDao
import com.example.navmbooks.database.entities.Chapter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ChapterRepository(private val chapterDao: ChapterDao) {
    private val coroutineScope = CoroutineScope(Dispatchers.Main)

    // Insert chapters into the database
    fun insertChapters(chapters: List<Chapter>) {
        coroutineScope.launch(Dispatchers.IO) {
            chapterDao.insertChapters(chapters)
        }
    }

    // Get all chapters for a specific book
    fun getChaptersByBook(bookId: Int): List<Chapter> {
        return chapterDao.getChaptersByBook(bookId)
    }
}
