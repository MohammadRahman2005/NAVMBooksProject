package com.example.navmbooks.database

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.room.Room
import com.example.navmbooks.database.dao.AuthorDao
import com.example.navmbooks.database.entities.Author
import com.example.navmbooks.database.repository.AuthorRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class MainActivity : AppCompatActivity() {

    private lateinit var authorDao: AuthorDao
    private lateinit var authorRepository: AuthorRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize Room Database and DAO
        val db = Room.databaseBuilder(applicationContext, AppDatabase::class.java, "app-database").build()
        authorDao = db.authorDao()
        authorRepository = AuthorRepository(authorDao)

        // Insert an author and then fetch it
        CoroutineScope(Dispatchers.IO).launch {
            // Insert an Author
            val newAuthor = Author(authorName = "Jane Austen")
            authorRepository.insertAuthor(newAuthor)

            // Retrieve the author by ID (assuming ID is 1)
            val fetchedAuthor = authorRepository.getAuthorById(1)
            withContext(Dispatchers.Main) {
                Log.d("MainActivity", "Fetched Author - ID: ${fetchedAuthor.authorId}, Name: ${fetchedAuthor.authorName}")
            }
        }
    }
}



