package com.example.navmbooks.database.repository

import com.example.navmbooks.database.dao.AuthorDao
import com.example.navmbooks.database.entities.Author
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AuthorRepository(private val authorDao: AuthorDao) {
    private val coroutineScope = CoroutineScope(Dispatchers.Main)

    // Insert a single author (runs on IO dispatcher)
    suspend fun insertAuthor(author: Author): Int {
        return withContext(Dispatchers.IO) {
            authorDao.insertAuthor(author)
        }
    }

    // Get an author by ID (runs on IO dispatcher)
    suspend fun getAuthorById(authorId: Int): Author {
            return authorDao.getAuthorById(authorId)
    }
}
