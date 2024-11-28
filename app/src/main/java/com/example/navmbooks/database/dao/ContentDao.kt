package com.example.navmbooks.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.lifecycle.LiveData
import com.example.navmbooks.database.ContentWithChapterInfo
import com.example.navmbooks.database.entities.Content

@Dao
interface ContentDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertContents(contents: List<Content>)

    @Query("""
        SELECT 
            contentId, contentType, chapterContent
        FROM 
            contents
        WHERE 
            chapterId = :chapterId
        ORDER BY 
            contentId
    """)
    fun getContentByChapter(chapterId: Int): LiveData<List<Content>>

    @Query("""
        SELECT 
            ct.contentId, ct.contentType, ct.chapterContent, ch.chapterTitle, ch.chapterNumber
        FROM 
            contents ct
        JOIN 
            chapters ch ON ct.chapterId = ch.chapterId
        WHERE 
            ch.bookId = :bookId AND ct.chapterContent LIKE '%' || :keyword || '%'
    """)
    fun searchContentInBook(bookId: Int, keyword: String): LiveData<List<ContentWithChapterInfo>>
}
