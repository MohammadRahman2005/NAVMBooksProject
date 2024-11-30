package com.example.navmbooks.database.repository

import androidx.lifecycle.LiveData
import com.example.navmbooks.database.ContentWithChapterInfo
import com.example.navmbooks.database.dao.ContentDao
import com.example.navmbooks.database.entities.Content
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ContentRepository(private val contentDao: ContentDao) {
    private val coroutineScope = CoroutineScope(Dispatchers.Main)

    // Insert content into the database
    fun insertContents(contents: List<Content>) {
        coroutineScope.launch(Dispatchers.IO) {
            contentDao.insertContents(contents)
        }
    }

    // Get all content for a specific chapter
    fun getContentByChapter(chapterId: Int): List<Content> {
            return contentDao.getContentByChapter(chapterId)
    }

    // Search content in a book by keyword
    fun searchContentInBook(bookId: Int, keyword: String): List<ContentWithChapterInfo> {
            return contentDao.searchContentInBook(bookId, keyword)
    }
}
