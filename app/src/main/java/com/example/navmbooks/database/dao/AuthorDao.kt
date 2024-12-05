package com.example.navmbooks.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.navmbooks.database.entities.Author

@Dao
interface AuthorDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAuthor(author: Author): Long

    @Query("SELECT * FROM authors WHERE authorId = :authorId")
    suspend fun getAuthorById(authorId: Int): Author
}
