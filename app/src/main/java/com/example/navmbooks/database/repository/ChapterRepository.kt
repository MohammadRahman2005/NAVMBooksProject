package com.example.navmbooks.database.repository

import android.content.Context
import androidx.lifecycle.LiveData
import com.example.navmbooks.database.DatabaseProvider
import com.example.navmbooks.database.entities.Chapter

class ChapterRepository(context: Context) {
    private val chapterDao = DatabaseProvider.getDatabase(context).chapterDao()

    // Insert chapters into the database
    suspend fun insertChapters(chapters: List<Chapter>) = chapterDao.insertChapters(chapters)

    // Get all chapters for a specific book
    fun getChaptersByBook(bookId: Int): LiveData<List<Chapter>> = chapterDao.getChaptersByBook(bookId)
}
