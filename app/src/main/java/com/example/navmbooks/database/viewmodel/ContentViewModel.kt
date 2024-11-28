package com.example.navmbooks.database.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.navmbooks.database.ContentWithChapterInfo
import com.example.navmbooks.database.entities.Content
import com.example.navmbooks.database.repository.ContentRepository
import kotlinx.coroutines.launch

class ContentViewModel(private val repository: ContentRepository) : ViewModel() {

    // LiveData for all content in a specific chapter
    fun getContentByChapter(chapterId: Int): LiveData<List<Content>> = repository.getContentByChapter(chapterId)

    // LiveData for searching content within a book by keyword
    fun searchContentInBook(bookId: Int, keyword: String): LiveData<List<ContentWithChapterInfo>> =
        repository.searchContentInBook(bookId, keyword)

    // Insert content into the database
    fun insertContents(contents: List<Content>) {
        viewModelScope.launch {
            repository.insertContents(contents)
        }
    }
}
