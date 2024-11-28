package com.example.navmbooks.database.repository

import com.example.navmbooks.database.dao.AuthorDao
import com.example.navmbooks.database.entities.Author

class AuthorRepository(private val authorDao: AuthorDao) {

    // Insert a list of authors
    suspend fun insertAuthor(author: Author) {
        authorDao.insertAuthor(author)
    }

    // Get an author by ID
    suspend fun getAuthorById(authorId: Int): Author {
        return authorDao.getAuthorById(authorId)
    }
}
