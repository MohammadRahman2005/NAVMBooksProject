package com.example.navmbooks.database.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.example.navmbooks.database.entities.Author
import com.example.navmbooks.database.repository.AuthorRepository
import kotlinx.coroutines.launch

class AuthorViewModel(private val repository: AuthorRepository) : ViewModel() {

    // Insert a single author
    fun insertAuthor(author: Author) {
        viewModelScope.launch {
            repository.insertAuthor(author)
        }
    }

    // Get an author by ID
    fun getAuthorById(authorId: Int): LiveData<Author> = liveData {
        val author = repository.getAuthorById(authorId)
        emit(author)
    }
}
