package com.example.navmbooks.database.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.navmbooks.database.repository.AuthorRepository
import com.example.navmbooks.database.viewmodel.AuthorViewModel

class AuthorViewModelFactory(private val repository: AuthorRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AuthorViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return AuthorViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}