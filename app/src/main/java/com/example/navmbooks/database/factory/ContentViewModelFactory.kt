package com.example.navmbooks.database.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.navmbooks.database.repository.ContentRepository
import com.example.navmbooks.database.viewmodel.ContentViewModel

class ContentViewModelFactory(private val repository: ContentRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ContentViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ContentViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
