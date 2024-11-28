package com.example.navmbooks.database.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.navmbooks.database.entities.Chapter
import com.example.navmbooks.database.repository.ChapterRepository
import kotlinx.coroutines.launch

class ChapterViewModel(private val repository: ChapterRepository) : ViewModel() {

    // LiveData for all chapters of a specific book
    fun getChaptersByBook(bookId: Int): LiveData<List<Chapter>> = repository.getChaptersByBook(bookId)

    // Insert chapters into the database
    fun insertChapters(chapters: List<Chapter>) {
        viewModelScope.launch {
            repository.insertChapters(chapters)
        }
    }
}
