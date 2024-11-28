package com.example.navmbooks.database.repository

import android.content.Context
import androidx.lifecycle.LiveData
import com.example.navmbooks.database.ContentWithChapterInfo
import com.example.navmbooks.database.DatabaseProvider
import com.example.navmbooks.database.entities.Content

class ContentRepository(context: Context) {
    private val contentDao = DatabaseProvider.getDatabase(context).contentDao()

    // Insert content into the database
    suspend fun insertContents(contents: List<Content>) = contentDao.insertContents(contents)

    // Get all content for a specific chapter
    fun getContentByChapter(chapterId: Int): LiveData<List<Content>> = contentDao.getContentByChapter(chapterId)

    // Search content in a book by keyword
    fun searchContentInBook(bookId: Int, keyword: String): LiveData<List<ContentWithChapterInfo>> =
        contentDao.searchContentInBook(bookId, keyword)
}
