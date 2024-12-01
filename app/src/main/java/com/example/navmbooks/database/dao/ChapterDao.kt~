package com.example.navmbooks.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.lifecycle.LiveData
import com.example.navmbooks.database.entities.Chapter

@Dao
interface ChapterDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertChapters(chapters: Chapter): Int

    @Query("""
        SELECT 
            bookId, chapterId, chapterNumber, chapterTitle
        FROM 
            chapters
        WHERE 
            bookId = :bookId
        ORDER BY 
            chapterNumber
    """)
    fun getChaptersByBook(bookId: Int): List<Chapter>
}
