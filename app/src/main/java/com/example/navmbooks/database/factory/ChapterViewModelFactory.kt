package com.example.navmbooks.database.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.navmbooks.database.repository.ChapterRepository
import com.example.navmbooks.database.viewmodel.ChapterViewModel

class ChapterViewModelFactory(private val repository: ChapterRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ChapterViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ChapterViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
